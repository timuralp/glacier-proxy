package com.bouncestorage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;

import com.sun.net.httpserver.HttpServer;

public class GlacierProxy {
    private HttpServer server;
    private BlobStore blobStore;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new GlacierProxyHandler(this));
        server.setExecutor(null);
        server.start();
        BlobStoreContext context = ContextBuilder.newBuilder("transient").credentials("", "")
                .build(BlobStoreContext.class);
        blobStore = context.getBlobStore();
    }

    public void stop() {
        server.stop(0);
    }

    public Vault getVault(Map<String, String> parameters) {
        return new Vault(this);
    }

    public Archive getArchive(Map<String, String> parameters) {
        return new Archive(this);
    }

    public BlobStore getBlobStore() {
        return blobStore;
    }
}
