/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

/**
 * This fragment has a big [ImageView] that shows PDF pages, and 2 [Button]s to move between pages.
 */
class PdfRendererBasicFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pdf_renderer_basic_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // View references.
        val image: ImageView = view.findViewById(R.id.image)
        val buttonPrevious: Button = view.findViewById(R.id.previous)
        val buttonNext: Button = view.findViewById(R.id.next)

        // Bind data.
        val viewModel = ViewModelProviders.of(this).get(PdfRendererBasicViewModel::class.java)
        viewModel.pageInfo.observe(viewLifecycleOwner, Observer { (index, count) ->
            activity?.title = getString(R.string.app_name_with_index, index + 1, count)
        })
        viewModel.pageBitmap.observe(viewLifecycleOwner, Observer { image.setImageBitmap(it) })
        viewModel.previousEnabled.observe(viewLifecycleOwner, Observer {
            buttonPrevious.isEnabled = it
        })
        viewModel.nextEnabled.observe(viewLifecycleOwner, Observer {
            buttonNext.isEnabled = it
        })

        // Bind events.
        buttonPrevious.setOnClickListener { viewModel.showPrevious() }
        buttonNext.setOnClickListener { viewModel.showNext() }
    }

}
