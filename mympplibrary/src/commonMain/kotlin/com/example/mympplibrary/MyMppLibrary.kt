package com.example.mympplibrary

import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.api.NormalizedCache
import com.apollographql.apollo3.cache.normalized.apolloStore
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class MyMppLibrary {
    suspend fun executeSampleQuery(): List<LaunchListQuery.Launch?> {
        return getApolloClient()
            .query(LaunchListQuery())
            .execute()
            .dataOrThrow()
            .launches
            .launches
    }

    fun executeSampleFlowQuery(): Flow<LaunchListQuery.Launches> {
        return getApolloClient()
            .query(LaunchListQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .map {
                it.dataOrThrow().launches
            }
    }

    suspend fun getNormalizedCacheString(): String {
        val dump = getApolloClient().apolloStore.dump()
        return NormalizedCache.prettifyDump(dump)
    }
}

class KotlinNativeFlowWrapper<T>(private val flow: Flow<T>) {
    fun subscribe(
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit,
    ) = flow
        .onEach { onEach(it) }
        .catch { onThrow(it) }
        .onCompletion { onComplete() }
        .launchIn(MainScope())
}
