package ru.virgil.testutils.fluent_request;

public interface BodyStepJsonStart {

    BodyStepJson send(Object dto);

    BodyStepJson receive(Class<?> responseClass, Class<?>... responseClasses);

    BodyStepJson receiveAsBytes();

    BodyStepJson exchange(Object dto, Class<?> responseClass, Class<?>... responseClasses);

    FinalStep<?> and() throws Exception;
}
