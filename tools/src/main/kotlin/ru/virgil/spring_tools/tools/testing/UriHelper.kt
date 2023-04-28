package ru.virgil.spring_tools.tools.testing

import org.apache.http.client.utils.URIBuilder

interface UriHelper {

    fun uri() = URIBuilder()

}
