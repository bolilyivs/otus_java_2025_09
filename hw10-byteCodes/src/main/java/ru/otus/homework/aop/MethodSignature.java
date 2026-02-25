package ru.otus.homework.aop;

import java.util.Collection;

public record MethodSignature(String name, Collection<String> paramType) {}
