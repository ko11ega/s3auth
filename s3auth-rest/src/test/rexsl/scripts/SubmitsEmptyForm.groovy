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
package com.s3auth.rest.rexsl.scripts

import com.jcabi.manifests.Manifests
import com.rexsl.page.auth.AuthInset
import com.rexsl.test.RestTester
import com.s3auth.hosts.UserMocker
import com.s3auth.rest.RestUser
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import org.hamcrest.Matchers

Manifests.append(new File(rexsl.basedir, 'target/test-classes/META-INF/MANIFEST.MF'))

def user = new RestUser(new UserMocker().withIdentity('urn:facebook:999').mock())
def cookie = 'Rexsl-Auth=' + AuthInset.encrypt(
    user.asIdentity(),
    Manifests.read('S3Auth-SecurityKey'),
    Manifests.read('S3Auth-SecuritySalt')
)

RestTester.start(rexsl.home)
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .header(HttpHeaders.COOKIE, cookie)
    .get('read home page')
    .rel('/page/links/link[@rel="add"]/@href')
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .header(HttpHeaders.COOKIE, cookie)
    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
    .post('submit incomplete form', 'host=test-3.s3auth.com&key=')
    .assertStatus(HttpURLConnection.HTTP_SEE_OTHER)
    .assertHeader(
        HttpHeaders.SET_COOKIE,
        Matchers.hasItem(Matchers.containsString('Rexsl-Flash'))
    )
    .follow()
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    .header(HttpHeaders.COOKIE, cookie)
    .get('read home page again')
    .assertXPath('/page/flash[level="WARNING"]')
    .assertXPath('/page/flash/message')
