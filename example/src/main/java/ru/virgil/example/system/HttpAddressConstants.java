package ru.virgil.example.system;

/**
 * Можно выделять постоянно повторяемые в API параметры в отдельный интерфейс.
 * Это защитит от расхождения в названиях, типа pages и page.
 * И позволит не повторять названия параметров в каждом контроллере.
 */
public interface HttpAddressConstants {

    String PAGE_PARAM = "page";
    String PAGE_SIZE_PARAM = "size";
}
