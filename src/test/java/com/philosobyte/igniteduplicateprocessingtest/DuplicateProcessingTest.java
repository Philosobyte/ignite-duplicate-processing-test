package com.philosobyte.igniteduplicateprocessingtest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.util.Random;

@Slf4j
@SpringBootTest(classes = DuplicateProcessingTest.DuplicateProcessingTestConfiguration.class)
class DuplicateProcessingTest extends GridCommonAbstractTest {
    // cache key
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LightsaberWielder {
        String name;
    }

    // cache value
    public enum LightsaberColor {
        BLUE, RED
    }

    // entry processor
    public static class LightsaberRecordsEntryProcessor implements CacheEntryProcessor<LightsaberWielder, LightsaberColor, LightsaberColor> {
        private static final Random random = new Random();

        private Ignite ignite;

        @IgniteInstanceResource
        public void setIgnite(Ignite ignite) {
            this.ignite = ignite;
        }

        @Override
        public LightsaberColor process(MutableEntry<LightsaberWielder, LightsaberColor> entry, Object... arguments) throws EntryProcessorException {
            LightsaberWielder lightsaberUser = entry.getKey();
            LightsaberColor existingColor = entry.getValue();
            log.info(
                "Processing on node {} for key {} and existing value {} and random int: {}",
                ignite.cluster().localNode().id(), lightsaberUser, existingColor, random.nextInt()
            );

            LightsaberColor replacementColor = getRandomColor();
            log.info("Setting color to {}", replacementColor);
            entry.setValue(replacementColor);

            return replacementColor;
        }

        private LightsaberColor getRandomColor() {
            int i = random.nextInt(0, LightsaberColor.values().length);
            return LightsaberColor.values()[i];
        }
    }

    // Spring config to set up test
    @Configuration
    static class DuplicateProcessingTestConfiguration {
        @Bean
        @SneakyThrows
        public Ignite ignite(ApplicationContext applicationContext) {
            IgniteConfiguration cfg = new IgniteConfiguration();
            return IgniteSpring.start(cfg, applicationContext);
        }

        @Bean
        public IgniteCache<LightsaberWielder, LightsaberColor> lightsaberColorsCache(Ignite ignite) {
            CacheConfiguration<LightsaberWielder, LightsaberColor> cacheConfig = new CacheConfiguration<>();
            cacheConfig.setName("lightsaber-colors");
            cacheConfig.setCacheMode(CacheMode.PARTITIONED);

            // make sure there are no backup partitions for an entry processor to execute on
            cacheConfig.setBackups(0);
            // even if there were backups, in ATOMIC mode, an entry processor should only execute on the primary partition
            cacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);

            return ignite.getOrCreateCache(cacheConfig);
        }

        @Bean
        public LightsaberRecordsEntryProcessor lightsaberRecordsEntryProcessor() {
            return new LightsaberRecordsEntryProcessor();
        }
    }

    // start one server node for the test
    private static DuplicateProcessingTest duplicateProcessingTest;
    static {
        try {
            duplicateProcessingTest = new DuplicateProcessingTest();
            duplicateProcessingTest.beforeFirstTest();
            duplicateProcessingTest.startGrid();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start test server grid", e);
        }
    }

    @Autowired
    private LightsaberRecordsEntryProcessor lightsaberRecordsEntryProcessor;

    @Autowired
    private IgniteCache<LightsaberWielder, LightsaberColor> lightsaberRecordsCache;

    @Test
    void test() {
        LightsaberWielder anakin = new LightsaberWielder("anakin");
        LightsaberColor replacementColor = lightsaberRecordsCache.invoke(anakin, lightsaberRecordsEntryProcessor);
        log.info("replacementColor: {}", replacementColor);

        // No assertions here to keep this example simple, but...
        // Log statements show that lightsaberRecordsEntryProcessor.process executes twice, even though
        // the entry processor is invoked only once during the test.

        // The return value, titled replacementColor, always comes from the second execution.
    }
}
