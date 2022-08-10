/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pdfrendererbasic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PdfRendererBasicViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = PdfRendererBasicViewModel(
        ApplicationProvider.getApplicationContext(),
        true
    )

    @Test
    fun allPages() {
        assertThat(viewModel).isNotNull()
        assertThat(viewModel.pageInfo.value).isEqualTo(0 to 10)
        assertThat(viewModel.previousEnabled.value).isFalse()
        assertThat(viewModel.nextEnabled.value).isTrue()
        assertThat(viewModel.pageBitmap.value).isNotNull()
        repeat(9) { viewModel.showNext() }
        assertThat(viewModel.pageInfo.value).isEqualTo(9 to 10)
        assertThat(viewModel.previousEnabled.value).isTrue()
        assertThat(viewModel.nextEnabled.value).isFalse()
        assertThat(viewModel.pageBitmap.value).isNotNull()
    }

}
