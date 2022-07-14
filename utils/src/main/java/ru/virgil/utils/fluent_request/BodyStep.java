package ru.virgil.utils.fluent_request;

public interface BodyStep {

    FinalStep<?> and() throws Exception;
}
