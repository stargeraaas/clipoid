package dev.sukharev.clipangel.presentation.view.info

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.utils.getResourceOrNullAttr
import dev.sukharev.clipangel.utils.getStringOrNullAttr

class InformationView: LinearLayout {

    private var image: ImageView? = null
    private var titleTextView: TextView? = null
    private var subtitleTextView: TextView? = null
    private var submitButton: Button? = null

    private var viewState = ViewState()

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.InformationView)
        val resourceLayout = R.layout.layout_simple_template

        val rootView = LayoutInflater.from(context).inflate(resourceLayout, this)

        rootView.apply {
            image = findViewById(R.id.image)
            titleTextView = findViewById(R.id.title_text)
            subtitleTextView = findViewById(R.id.subtitle_text)
            submitButton = findViewById(R.id.submit_button)
        }

        viewState.titleText = getStringOrNullAttr(attrs, R.styleable.InformationView_titleText)
        viewState.subtitleText = getStringOrNullAttr(attrs, R.styleable.InformationView_subtitleText)
        viewState.submitButtonText = getStringOrNullAttr(attrs, R.styleable.InformationView_buttonText)
        viewState.imageSrc = getResourceOrNullAttr(attrs, R.styleable.InformationView_imageSrc)

        attrs.recycle()

        changeState(viewState)
    }

    init {

    }

    fun setOnClickSubmitButtonListener(onClickListener: OnClickListener) {
        submitButton?.setOnClickListener(onClickListener)
    }

    fun removeOnClickSubmitButtonListener() {
        submitButton?.setOnClickListener(null)
    }

    fun changeState(state: ViewState) {
        viewState = state
        viewState.imageSrc?.let {
            image?.setImageResource(it)
            image?.visibility = View.VISIBLE
        } ?: run { image?.visibility = View.GONE}

        viewState.titleText?.let {
            titleTextView?.visibility = View.VISIBLE
            titleTextView?.text = it
        } ?: run {
            titleTextView?.visibility = View.GONE
        }

        viewState.subtitleText?.let {
            subtitleTextView?.visibility = View.VISIBLE
            subtitleTextView?.text = it
        } ?: run {
            subtitleTextView?.visibility = View.GONE
        }

        viewState.submitButtonText?.let {
            submitButton?.text = it
            submitButton?.visibility = View.VISIBLE
        } ?: run {
            submitButton?.visibility = View.GONE
        }
    }

    data class ViewState(
            var titleText: String? = null,
            var subtitleText: String? = null,
            var submitButtonText: String? = null,
            var imageSrc: Int? = null
    )

}