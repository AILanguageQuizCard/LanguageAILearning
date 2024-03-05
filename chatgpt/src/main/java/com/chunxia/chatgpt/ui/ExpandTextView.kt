package com.chunxia.chatgpt.ui

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.text.SpannableString
import android.text.Spanned
import android.text.StaticLayout
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.chunxia.chatgpt.R

/**
 * Desc 支持展开收起的TextView
 * Author ZY
 * Date 2021/10/10 10:30
 */
class ExpandTextView(var context: Context) {

    // 默认展示行数，超过多少行隐藏
    private var maxLine: Int = 2

    // 文本左右边距总合
    private var margin: Int = 0

    // 展开、收起文本颜色
    private var colorStr: String = "#0079e2"

    // 展开显示文本
    private var expandStr: String = "展开"

    // 收起显示文本
    private var foldStr: String = "收起"


    private var content: String = ""

    fun getContent(): String {
        return content
    }

    fun setMaxLine(maxLine: Int): ExpandTextView {
        this.maxLine = maxLine
        return this
    }

    fun setMargin(margin: Int): ExpandTextView {
        this.margin = margin
        return this
    }

    fun setColorStr(colorStr: String): ExpandTextView {
        this.colorStr = colorStr
        return this
    }


    fun initDefaultSetting() {
        expandStr = context.resources.getString(R.string.expand_text_view_expandStr)
        foldStr = context.resources.getString(R.string.expand_text_view_foldStr)
    }


    fun show(expandTextView: TextView, content: String) {
        initDefaultSetting()

        // 裁剪函数来去除前导空格
        expandTextView.text = content.trimIndent()

        // 获取TextView的画笔对象
        val paint = expandTextView.paint

        // 每行文本布局宽度: 屏幕宽度 - 左右边距
        val width = context.resources.displayMetrics.widthPixels - context.resources.getDimension(margin).toInt()

        val fontColor = ForegroundColorSpan(Color.parseColor(colorStr))
        // 实例化StaticLayout
        val staticLayout = StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        // 判断content行数是否超过最大限制行数
        if (staticLayout.lineCount > maxLine) {
            /**
             * 展开后的文本内容
             */
            val str = ("$content\t\t$foldStr")
            val expandSpanStr = SpannableString(str)
            expandSpanStr.setSpan(fontColor, str.length - 2, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //获取最后一个文字的下标
            val index = staticLayout.getLineStart(maxLine) - 1

            /**
             * 收起后的文本内容
             */
            val str2 = ("${content.substring(0, index - 2)}...$expandStr")
            val foldSpanStr = SpannableString(str2)
            foldSpanStr.setSpan(fontColor, str2.length - 2, str2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //设置收起后的文本内容
            expandTextView.text = foldSpanStr
            expandTextView.setOnClickListener {
                if (expandTextView.isSelected) { //收起的状态
                    expandTextView.text = expandSpanStr
                    expandTextView.isSelected = false
                } else { //展开的状态
                    expandTextView.text = foldSpanStr
                    expandTextView.isSelected = true
                }
            }
            //设成选中状态 true收起状态,false展示状态
            expandTextView.isSelected = true
        } else {
            //未超过最大限制行数，直接设置文本
            expandTextView.text = content
            expandTextView.setOnClickListener(null)
        }
        this.content = content
    }
}
