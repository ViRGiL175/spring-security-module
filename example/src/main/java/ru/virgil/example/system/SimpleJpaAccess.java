package ru.virgil.example.system;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Этот интерфейс позволяет открыть доступ к базовым функциям JPA-репозитория сервиса.
 * <p>
 * Поможет сократить код и не писать обертки для простых операций, типа save, saveAll, delete и т.д.
 * При этом сложные запросы, типа findByOwnerAndTruckOrderByBoxDate не будут открыты наружу.
 * <p>
 * Если в домене простые данные, и к ним нужен простой доступ – используем этот интерфейс.
 * Если в домене сложные данные со сложным доступом – доступ к JPA-функциям лучше не открывать.
 */
public interface SimpleJpaAccess<T extends IdentifiedEntity, ID> {

    JpaRepository<T, ID> getRepository();

}
