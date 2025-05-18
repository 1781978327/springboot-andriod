package com.example.ai_1.util;

import io.noties.prism4j.GrammarLocator;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class CustomGrammarLocator implements GrammarLocator {
    @Nullable
    @Override
    public Prism4j.Grammar grammar(@NotNull Prism4j prism4j, @NotNull String language) {
        return null;
    }

    @NotNull
    @Override
    public Set<String> languages() {
        return Collections.emptySet();
    }
} 