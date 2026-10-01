# اپلیکیشن هنرستان نوشیروانی ۴۱۳ بابل

نسخه Android متصل به سایت هنرستان.

## منبع محتوا

محتوای زنده برنامه از سایت هنرستان دریافت می‌شود:
https://noshirvani-413.blogfa.com/

بنابراین اخبار، برنامه‌ها، گزارش‌ها و سایر صفحات سایت بدون ساخت مجدد APK قابل به‌روزرسانی هستند.

## ساخت APK در GitHub Actions

پس از قرار گرفتن فایل‌های این پروژه در شاخه `main`، Workflow با نام `Build Noshirvani 413 APK` اجرا می‌شود.

فایل APK در بخش Artifacts با نام `Noshirvani413-APK` قرار می‌گیرد.

## ساختار پروژه

- `app/` کد و منابع برنامه
- `.github/workflows/build-apk.yml` ساخت خودکار APK
- `settings.gradle.kts` تنظیم پروژه
- `build.gradle.kts` تنظیم پلاگین Android و Kotlin
