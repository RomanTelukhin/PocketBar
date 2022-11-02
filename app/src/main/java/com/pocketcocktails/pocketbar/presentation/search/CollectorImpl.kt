package com.pocketcocktails.pocketbar.presentation.search

import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.Executors

interface Collector {
    suspend fun collect(): String
}

class ServerRequestCollector : Collector {

    override suspend fun collect(): String {

        return withContext(Dispatchers.IO) {
            Timber.d("Test -----: ServerRequestCollector - Started")
            delay(3000)
            //server request
            Timber.d("Test -----: ServerRequestCollector - Completed")
            "Server Request"
        }
    }
}

class ReadFileCollector : Collector {

    override suspend fun collect(): String {
        return withContext(Dispatchers.IO) {
            Timber.d("Test -----: ReadFileCollector - Started")
            delay(1000)
            //read file
            Timber.d("Test -----: ReadFileCollector - Completed")
            "File"
        }
    }
}

class HardcodeCollector : Collector {

    override suspend fun collect(): String {
        return withContext(Dispatchers.Main) {
            Timber.d("Test -----: HardcodeCollector - Started")
            delay(5000)
            //return hardcode
            Timber.d("Test -----: HardcodeCollector - Completed")
            "Hardcoded String"
        }
    }
}

class DifferentCollector(private vararg val collectors: Collector) : Collector {
    override suspend fun collect(): String =
        coroutineScope {
            collectors
                .map { item -> async { item.collect() } }
                .fold(
                    initial = StringBuilder(),
                    operation = { total, async ->
                        total.append("${async.await()}- ")
                    }
                )
        }.toString()
}

data class Photo(val photoName: String)

class PhotoRecognizer {
    /*Проблема, которую надо решить - переиспользовать экземпляры нейро
    из-за слишком долгой инициализации
    и не позволять множеству потоков заходить в один экземпляр нейро.
    Один экземпляр - один поток, пока результат не будет получен
    Каждую фотку ты теперь 2 секунды обрабатываешь
    и очень много памяти выделаешь под нейро
    */
    private val ownDispatcher =
        Executors
            .newFixedThreadPool(2)
            .asCoroutineDispatcher()

    private val neuroObject: ThreadLocal<Neuro> =
        object : ThreadLocal<Neuro>() {
            override fun initialValue(): Neuro = Neuro()
        }

    suspend fun processPhoto(): List<String> = coroutineScope {
        return@coroutineScope arrayListOf<Any>(
            Photo("Name - 1"),
            Photo("Name - 2"),
            Photo("Name - 3"),
            Photo("Name - 4"),
            Photo("Name - 5"),
            Photo("Name - 6"),
            Photo("Name - 7"),
            Photo("Name - 8"),
            Photo("Name - 9"),
            Photo("Name - 10")
        )
            .map { bitmap ->
                async { recognize(bitmap) } }
            .awaitAll()
    }

//    private suspend fun recognize(photoBitmap: Any): String =
//        withContext(ownDispatcher + neuroObject.asContextElement()) {
//            neuroObject.get()?.recognize(photoBitmap) ?: Neuro().recognize(photoBitmap)
//        }

    private suspend fun recognize(photoBitmap: Any): String =
        withContext(context = ownDispatcher) {
            val neuro = withContext(context = neuroObject.asContextElement()) {
                neuroObject.get() ?: throw IllegalStateException()
            }
            neuro.recognize(photoBitmap)
        }
}

class Neuro {

    init { Thread.sleep(2000) }

    suspend fun recognize(photoBitmap: Any): String = photoBitmap.toString()
}


