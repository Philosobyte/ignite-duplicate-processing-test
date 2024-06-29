package com.philosobyte.igniteduplicateprocessingtest;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.springframework.stereotype.Component;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.util.Random;
import java.util.UUID;

import com.philosobyte.igniteduplicateprocessingtest.DuplicateProcessingTest.*;

@Slf4j
@Component
public class LightsaberRecordsEntryProcessor implements CacheEntryProcessor<LightsaberWielder, LightsaberColor, LightsaberColor> {
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
