package com.example.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.auth.Auth

object SupabaseClientProvider {

    private var client: io.github.jan.supabase.SupabaseClient? = null

    fun getInstance(supabaseUrl: String, supabaseKey: String): io.github.jan.supabase.SupabaseClient {
        if (client == null) {
            client = createSupabaseClient(
                supabaseUrl = supabaseUrl,
                supabaseKey = supabaseKey
            ) {
                install(Postgrest)
                install(Realtime)
                install(Storage)
                install(Auth)
            }
        }
        return client!!
    }

    fun reset() {
        client = null
    }
}
