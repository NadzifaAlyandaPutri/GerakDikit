# Tugas Praktikum - Aplikasi Kartu Atlet & Activity Lifecycle

Aplikasi **Kartu Atlet & Portofolio Olahraga** berbasis Android, dikembangkan menggunakan Kotlin dan XML di Android Studio. Aplikasi dijalankan di smartphone (vivo V2310, Android 15 / API 35) melalui ADB, dan setiap callback lifecycle dicatat ke Logcat dengan tag `LifecycleDemo`.

## Fitur & Layar Aplikasi

1. **Layar 1 (MainActivity)**: Kartu Atlet berisi foto profil (ikon bola), nama, cabang olahraga, bio, penghitung **Push-up** dan **Level**, label state lifecycle, form edit data (nama & cabang olahraga), tombol Reset, Simpan Profil, dan Lihat Portofolio.
2. **Layar 2 (SecondActivity)**: Statistik & Portofolio berisi total push-up sesi ini (dikirim dari Layar 1 lewat Intent), target lari mingguan, dan skill set atlet.

## Komponen UI yang Digunakan

- **ViewGroup**: LinearLayout (vertical & horizontal), ScrollView
- **View Widgets**: TextView, EditText, ImageView, Button
- **Resource Management**: strings.xml, colors.xml, drawable (shape & vector)

## Cara Menjalankan

1. Build & install ke HP lewat terminal: `./gradlew installDebug` (atau tombol Run di Android Studio).
2. Amati lifecycle: `adb logcat -s LifecycleDemo`
3. Buka Layar Kedua via ADB: `adb shell am start -n com.example.lifecyledemo/.SecondActivity`

## Tangkapan Layar (Screenshot)

| Layar 1 (Kartu Atlet) | Layar 2 (Portofolio) |
|---|---|
| ![Layar 1](screen1.jpeg) | ![Layar 2](screen2.jpeg) |

## Bukti Log Lifecycle

Semua log di bawah diambil dari perangkat nyata dengan `adb logcat -s LifecycleDemo`.

### Skenario 0 - Buka aplikasi dari launcher

```
15:33:17.813 I LifecycleDemo: MainActivity onCreate (savedInstanceState=false)
15:33:17.828 I LifecycleDemo: MainActivity onStart
15:33:17.833 I LifecycleDemo: MainActivity onResume
```

### Skenario A - Tekan tombol Home

```
15:36:35.133 I LifecycleDemo: MainActivity onPause
15:36:35.158 I LifecycleDemo: MainActivity onStop
15:36:35.206 I LifecycleDemo: MainActivity onSaveInstanceState
```

### Skenario B - Kembali ke aplikasi dari recent apps

```
15:36:48.107 I LifecycleDemo: MainActivity onRestart
15:36:48.161 I LifecycleDemo: MainActivity onStart
15:36:48.189 I LifecycleDemo: MainActivity onResume
```

### Skenario C (tambahan) - Putar orientasi layar

```
15:34:51.025 I LifecycleDemo: MainActivity onPause
15:34:51.048 I LifecycleDemo: MainActivity onStop
15:34:51.108 I LifecycleDemo: MainActivity onSaveInstanceState
15:34:51.131 I LifecycleDemo: MainActivity onDestroy (isChangingConfigurations=true)
15:34:51.555 I LifecycleDemo: MainActivity onCreate (savedInstanceState=true)
15:34:51.563 I LifecycleDemo: MainActivity onStart
15:34:51.569 I LifecycleDemo: MainActivity onResume
```

### Skenario D (tambahan) - Pindah ke Layar 2 lalu kembali

```
15:35:13.214 I LifecycleDemo: MainActivity onPause
15:35:13.360 I LifecycleDemo: SecondActivity onCreate
15:35:13.365 I LifecycleDemo: SecondActivity onStart
15:35:13.367 I LifecycleDemo: SecondActivity onResume
15:35:13.936 I LifecycleDemo: MainActivity onStop
15:35:13.939 I LifecycleDemo: MainActivity onSaveInstanceState
...
15:35:24.922 I LifecycleDemo: SecondActivity onPause
15:35:24.957 I LifecycleDemo: MainActivity onRestart
15:35:24.961 I LifecycleDemo: MainActivity onStart
15:35:24.964 I LifecycleDemo: MainActivity onResume
15:35:25.415 I LifecycleDemo: SecondActivity onStop
15:35:25.422 I LifecycleDemo: SecondActivity onDestroy
```

## Analisis Singkat Urutan Callback

- **Skenario 0 (buka awal):** Activity baru dibuat sehingga lewat `onCreate` (UI dipasang, `savedInstanceState=false` karena belum ada data tersimpan), lalu `onStart` (layar mulai terlihat), dan `onResume` (layar aktif dan bisa diinteraksi).
- **Skenario A (Home):** Fokus hilang (`onPause`) lalu layar tidak lagi terlihat (`onStop`). `onDestroy` **tidak** terpanggil, jadi instance Activity tetap hidup di memori. `onSaveInstanceState` dipanggil setelah `onStop` karena perangkat berjalan di Android 9 ke atas (API 35), dan ini menyimpan hitungan push-up.
- **Skenario B (kembali):** Jalurnya `onRestart → onStart → onResume`, **tanpa** `onCreate`. Ini berbeda dari pembukaan pertama karena instance yang sama dipakai kembali dari kondisi *stopped*, jadi UI tidak dibuat ulang.
- **Skenario C (rotasi):** Perubahan konfigurasi membuat Activity dihancurkan (`onDestroy` dengan `isChangingConfigurations=true`) lalu dibuat ulang (`onCreate` dengan `savedInstanceState=true`). Nilai push-up tetap karena disimpan di `onSaveInstanceState` dan dipulihkan di `onCreate`.
- **Skenario D (dua Activity):** `MainActivity.onPause` terpanggil lebih dulu sebelum `SecondActivity` dibuat, sedangkan `MainActivity.onStop` baru terpanggil setelah `SecondActivity` benar-benar terlihat. Saat kembali, `SecondActivity.onPause` lebih dulu, lalu `MainActivity` melewati `onRestart → onStart → onResume`, dan terakhir `SecondActivity` di-`onStop` dan di-`onDestroy` karena `finish()`.

**Kesimpulan:** `onPause`/`onResume` terkait fokus (apakah layar di depan), sedangkan `onStop`/`onStart` terkait visibilitas. `onCreate`/`onDestroy` hanya terjadi saat instance dibuat dan dilepas, misalnya saat rotasi atau penutupan aplikasi. Karena itu pekerjaan yang hanya relevan saat layar terlihat sebaiknya dikelola di `onStart/onStop`, dan state penting disimpan di `onSaveInstanceState`.

## Catatan

- Log menggunakan level `Log.i` karena level `Log.d` tidak tampil di Logcat perangkat yang dipakai.
