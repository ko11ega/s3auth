/**
 * Copyright (c) 2012, s3auth.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the s3auth.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.s3auth.hosts;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.mockito.Mockito;

/**
 * Mocker of {@link Bucket}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class BucketMocker {

    /**
     * The mock.
     */
    private final transient Bucket bucket = Mockito.mock(Bucket.class);

    /**
     * Public ctor.
     */
    public BucketMocker() {
        this.withName("maven.s3auth.com");
        this.withRegion("s3-ap-southeast-1");
        this.withKey("AAAAAAAAAAAAAAAAAAAA");
        this.withSecret("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        final S3Object object = Mockito.mock(S3Object.class);
        Mockito.doReturn(
            new S3ObjectInputStream(
                IOUtils.toInputStream("TXT"),
                new HttpGet()
            )
        ).when(object).getObjectContent();
        final AmazonS3 client = Mockito.mock(AmazonS3.class);
        Mockito.doReturn(object).when(client)
            .getObject(Mockito.any(GetObjectRequest.class));
        this.withClient(client);
    }

    /**
     * With this name.
     * @param name The name
     * @return This object
     */
    public BucketMocker withName(final String name) {
        Mockito.doReturn(name).when(this.bucket).name();
        return this;
    }

    /**
     * With this key.
     * @param key The key
     * @return This object
     */
    public BucketMocker withKey(final String key) {
        Mockito.doReturn(key).when(this.bucket).key();
        return this;
    }

    /**
     * With this secret.
     * @param secret The secret
     * @return This object
     */
    public BucketMocker withSecret(final String secret) {
        Mockito.doReturn(secret).when(this.bucket).secret();
        return this;
    }

    /**
     * With this region.
     * @param region The region
     * @return This object
     */
    public BucketMocker withRegion(final String region) {
        Mockito.doReturn(region).when(this.bucket).region();
        return this;
    }

    /**
     * With this client.
     * @param client The client
     * @return This object
     */
    public BucketMocker withClient(final AmazonS3 client) {
        Mockito.doReturn(client).when(this.bucket).client();
        return this;
    }

    /**
     * Mock it.
     * @return The bucket
     */
    public Bucket mock() {
        return this.bucket;
    }

}
