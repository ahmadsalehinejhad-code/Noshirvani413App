package ir.noshirvani413.app

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private const val HOME = "https://noshirvani-413.blogfa.com/"
        private const val OFFICIAL = "https://noshirvani413.ir/"
    }

    private lateinit var root: FrameLayout
    private lateinit var webView: WebView
    private lateinit var homeView: ScrollView
    private lateinit var progress: ProgressBar

    private val siteHosts = setOf("noshirvani-413.blogfa.com", "noshirvani413.ir", "www.noshirvani413.ir", "dl.toolschi.com")

    data class MenuItem(val title: String, val icon: String, val url: String)

    private val menuItems = listOf(
        MenuItem("هنرستان در یک نما", "🏫", "$HOME/category/3"),
        MenuItem("همکاران هنرستان", "👨‍🏫", "$HOME/category/6"),
        MenuItem("چارت سازمانی", "🏢", "$HOME/category/9"),
        MenuItem("بازدید از هنرستان", "👀", "$HOME/category/14"),
        MenuItem("تقویم اجرایی و آیین‌نامه‌ها", "📅", "$HOME/category/4"),
        MenuItem("برنامه هفتگی", "🗓", "$HOME/category/22"),
        MenuItem("برنامه امتحانات", "📋", "$HOME/category/16"),
        MenuItem("کتب درسی", "📚", "$HOME/category/1"),
        MenuItem("آموزش", "🎓", "$HOME/category/19"),
        MenuItem("دست‌ساخته و پروژه‌ها", "⚙", "$HOME/category/5"),
        MenuItem("هنرجویان برتر", "🏆", "$HOME/category/18"),
        MenuItem("جشنواره‌ها و مسابقات", "🏅", "$HOME/category/10"),
        MenuItem("اطلاعیه و اخبارها", "📢", "$HOME/category/7"),
        MenuItem("گزارش عملکرد و فعالیت‌های هنرستان", "📊", "$HOME/category/23"),
        MenuItem("فعالیت‌های فرهنگی و پرورشی", "🕌", "$HOME/category/24"),
        MenuItem("جلسات", "👥", "$HOME/category/13"),
        MenuItem("پیش‌ثبت‌نام", "📝", "$HOME/category/2"),
        MenuItem("دانلود نرم‌افزار", "💾", "$HOME/category/17"),
        MenuItem("سامانه مای‌مدیو", "💻", "$HOME/category/20"),
        MenuItem("تماس با ما", "☎", "$HOME/contact"),
        MenuItem("پیشنهادات و انتقادات", "💬", "$HOME/contact"),
        MenuItem("درباره ما", "ℹ", "$HOME/about"),
        MenuItem("پروفایل", "👤", "$HOME/profile"),
        MenuItem("عضویت در خبرنامه", "✉", "$HOME/newsletter"),
        MenuItem("آرشیو", "🗂", "$HOME/archive"),
        MenuItem("جستجوی پیشرفته", "🔎", "$HOME/search")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(6, 54, 83)
        buildUi()
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun textView(text: String, size: Float, color: Int, bold: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
            typeface = if (bold) android.graphics.Typeface.create("sans", android.graphics.Typeface.BOLD) else android.graphics.Typeface.create("sans", android.graphics.Typeface.NORMAL)
            includeFontPadding = true
        }
    }

    private fun buildUi() {
        root = FrameLayout(this)
        root.setBackgroundColor(Color.rgb(245, 248, 250))
        setContentView(root)

        buildHome()
        buildWebView()
        showHome()
    }

    private fun buildHome() {
        homeView = ScrollView(this).apply { isFillViewport = true; setBackgroundColor(Color.rgb(245, 248, 250)) }
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; layoutDirection = View.LAYOUT_DIRECTION_RTL }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(20), dp(18), dp(20), dp(20))
            setBackgroundResource(ir.noshirvani413.app.R.drawable.top_bg)
        }
        val logo = ImageView(this).apply {
            setImageResource(R.drawable.school_logo)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        header.addView(logo, LinearLayout.LayoutParams(dp(94), dp(94)))
        header.addView(textView("هنرستان پسرانه دولتی نوشیروانی ۴۱۳ بابل", 21f, Color.WHITE, true).apply { gravity = Gravity.CENTER; setPadding(0, dp(5), 0, 0) }, LinearLayout.LayoutParams(-1, -2))
        header.addView(textView("شاخه فنی و حرفه‌ای | الکتروتکنیک و الکترونیک", 13f, Color.WHITE).apply { gravity = Gravity.CENTER; alpha = .92f }, LinearLayout.LayoutParams(-1, -2))
        page.addView(header)

        val status = TextView(this).apply {
            text = "●  محتوای برنامه از سایت هنرستان به‌صورت آنلاین دریافت می‌شود"
            textSize = 12f
            setTextColor(Color.rgb(21, 104, 93))
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(10), dp(12), dp(10))
            setBackgroundResource(R.drawable.pill_bg)
        }
        page.addView(status, LinearLayout.LayoutParams(-1, dp(42)).apply { setMargins(dp(16), dp(14), dp(16), dp(4)) })

        val gate = ImageView(this).apply { setImageResource(R.drawable.school_gate); scaleType = ImageView.ScaleType.CENTER_CROP }
        page.addView(gate, LinearLayout.LayoutParams(-1, dp(190)).apply { setMargins(dp(16), dp(8), dp(16), 0) })

        page.addView(textView("دسترسی سریع", 19f, Color.rgb(21,48,71), true).apply { setPadding(dp(18), dp(18), dp(18), dp(8)) }, LinearLayout.LayoutParams(-1, -2))
        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, dp(12), dp(8)) }
        val quick = menuItems.take(12)
        for (i in quick.indices step 2) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; layoutDirection = View.LAYOUT_DIRECTION_RTL }
            addCard(row, quick[i])
            if (i + 1 < quick.size) addCard(row, quick[i + 1])
            grid.addView(row, LinearLayout.LayoutParams(-1, dp(96)))
        }
        page.addView(grid)

        val latest = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(12), dp(16), dp(14)); setBackgroundResource(R.drawable.card_bg) }
        latest.addView(textView("آخرین مطالب سایت", 17f, Color.rgb(21,48,71), true))
        latest.addView(textView("برای مشاهده جدیدترین اخبار، گزارش‌ها، برنامه هفتگی و فعالیت‌های هنرستان، محتوای زنده سایت را باز کنید.", 13f, Color.rgb(108,122,137)).apply { setPadding(0, dp(7), 0, dp(8)) })
        val open = Button(this).apply { text = "مشاهده آخرین مطالب"; setTextColor(Color.WHITE); textSize = 13f; setBackgroundColor(Color.rgb(11,79,124)); setOnClickListener { openWeb(HOME) } }
        latest.addView(open, LinearLayout.LayoutParams(-1, dp(46)))
        page.addView(latest, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(16), dp(4), dp(16), dp(14)) })

        val bottom = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setBackgroundColor(Color.WHITE); setPadding(0, dp(6), 0, dp(6)) }
        addBottom(bottom, "⌂\nخانه") { showHome() }
        addBottom(bottom, "▣\nاخبار") { openWeb("$HOME/category/7") }
        addBottom(bottom, "⚙\nخدمات") { openWeb("$HOME/category/2") }
        addBottom(bottom, "☰\nمنو") { openWeb(HOME) }
        page.addView(bottom, LinearLayout.LayoutParams(-1, dp(64)))

        homeView.addView(page)
        root.addView(homeView, FrameLayout.LayoutParams(-1, -1))
    }

    private fun addCard(row: LinearLayout, item: MenuItem) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(6), dp(5), dp(6), dp(5))
            setBackgroundResource(R.drawable.card_bg)
            isClickable = true
            setOnClickListener { openWeb(item.url) }
        }
        card.addView(textView(item.icon, 25f, Color.rgb(11,79,124)).apply { gravity = Gravity.CENTER })
        card.addView(textView(item.title, 12f, Color.rgb(21,48,71), true).apply { gravity = Gravity.CENTER; maxLines = 2; ellipsize = android.text.TextUtils.TruncateAt.END })
        row.addView(card, LinearLayout.LayoutParams(0, -1, 1f).apply { setMargins(dp(5), dp(5), dp(5), dp(5)) })
    }

    private fun addBottom(parent: LinearLayout, label: String, action: () -> Unit) {
        val b = TextView(this).apply { text = label; textSize = 12f; setTextColor(Color.rgb(11,79,124)); gravity = Gravity.CENTER; setOnClickListener { action() } }
        parent.addView(b, LinearLayout.LayoutParams(0, -1, 1f))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun buildWebView() {
        webView = WebView(this).apply {
            setBackgroundColor(Color.WHITE)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.loadsImagesAutomatically = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(false)
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.userAgentString = settings.userAgentString + " Noshirvani413App/2.0"
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    return handleUrl(request.url.toString())
                }
                @Deprecated("Deprecated in API 24")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean = handleUrl(url)
                override fun onPageFinished(view: WebView, url: String) {
                    progress.visibility = View.GONE
                    injectPersianCss(view)
                }
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: android.webkit.WebResourceError) {
                    if (request.isForMainFrame) progress.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progress.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
                    progress.progress = newProgress
                }
            }
            setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
                try {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.addRequestHeader("User-Agent", userAgent)
                    request.setMimeType(mimeType)
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
                    request.setDescription("دانلود از سایت هنرستان نوشیروانی ۴۱۳")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType))
                    (getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
                    Toast.makeText(this@MainActivity, "دانلود آغاز شد", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            })
        }
        root.addView(webView, FrameLayout.LayoutParams(-1, -1))
        progress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progress.max = 100
        root.addView(progress, FrameLayout.LayoutParams(-1, dp(4), Gravity.TOP))
    }

    private fun handleUrl(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host?.lowercase() ?: ""
            if (host in siteHosts || host.endsWith(".blogfa.com")) {
                false
            } else if (url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("sms:")) {
                startActivity(Intent(Intent.ACTION_VIEW, uri)); true
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, uri)); true
            }
        } catch (_: Exception) { false }
    }

    private fun injectPersianCss(view: WebView) {
        val js = """
            (function(){
              var s=document.getElementById('n413-app-style');
              if(!s){s=document.createElement('style');s.id='n413-app-style';document.head.appendChild(s);}
              s.innerHTML='html,body,*{font-family:sans-serif!important;} body{direction:rtl!important;} img{max-width:100%!important;height:auto!important;} a{word-break:break-word!important;}';
            })();
        """.trimIndent()
        view.evaluateJavascript(js, null)
    }

    private fun openWeb(url: String) {
        homeView.visibility = View.GONE
        webView.visibility = View.VISIBLE
        progress.visibility = View.VISIBLE
        webView.loadUrl(url)
    }

    private fun showHome() {
        homeView.visibility = View.VISIBLE
        webView.visibility = View.GONE
    }

    override fun onBackPressed() {
        when {
            webView.visibility == View.VISIBLE && webView.canGoBack() -> webView.goBack()
            webView.visibility == View.VISIBLE -> showHome()
            else -> super.onBackPressed()
        }
    }
}
