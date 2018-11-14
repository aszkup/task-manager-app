package com.example.protonapp.di

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import org.kodein.di.Kodein
import org.kodein.di.generic.*

val dropboxModule = Kodein.Module {
    bind<DbxRequestConfig>() with provider {
        DbxRequestConfig.newBuilder("dropbox/java-tutorial").build()
    }
    bind<DbxClientV2>() with singleton {
        DbxClientV2(instance(), instance("dbx_access_token"))
    }

    //TODO access token should be provided by backend after any auth process
    constant("dbx_access_token") with
            "uCBRpM3tIaAAAAAAAAAACZR571eWWoBCU470OFXCPgjpf7nZEKhjuPWxU7hGjSHS"
}
