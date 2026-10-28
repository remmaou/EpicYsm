# Port YSM-Epic-Fight-Compat -> Fabric 1.21.1 (target: Fox Model Loader = mod id `yes_steve_model` 1.3.6)

Sumber: YSM-EpicFight-Compat (MIT, sakuraimikoto33), NeoForge 1.21.1, menargetkan YSM resmi lewat YSM Mapping API.
Ukuran snapshot ini: 263 file Java di main (43.596 baris), termasuk 27 deklarasi `@Mixin`.

## Tahap 1 (zip ini)
- Proyek Fabric (Loom, Mojang mappings) + workflow GitHub Actions. Jar Epic Fight Fabric dan Fox Model Loader ada di `libs/`.
- Import NeoForge dialihkan ke shim Epic Fight Fabric (IPayloadContext, PacketDistributor, PayloadRegistrar, bus, ClientTick/RenderFrame)
  dan shim kecil lokal (`fabric/shim`: Dist, OnlyIn, ModList, FMLPaths).
- File yang menyentuh NeoForge turun dari 54 menjadi 23 (daftar: SISA_FILE_NEOFORGE.txt). 6 di antaranya hanya memakai
  `ModConfigSpec`/`ModConfig` yang disediakan Forge Config API Port, jadi boleh tetap.
- Entrypoint sementara `CompatModFabric`: hanya mencatat log. Mod ini BELUM berfungsi dan belum dikompilasi.

## Sisa pekerjaan
1. Event NeoForge -> callback Fabric (belum ada di shim): ServerStopped, ServerTick, PlayerEvent (login/logout/clone),
   EntityLeaveLevel, EntityJoinLevel, EntityMount, LivingShieldBlock, TagsUpdated, Command, RenderPlayer, Screen,
   ClientPlayerNetwork, RegisterClientReloadListeners, FMLLoadComplete/ClientSetup, DamageContainer, CommonHooks.
2. `CompatMod` (@Mod) -> entrypoint Fabric penuh, registrasi payload (24 handler network memakai IPayloadContext shim).
3. Lapisan YSM: 5 file memakai YSM Mapping API (khusus YSM resmi yang diobfuscate) + 7 alias di `ysmref`.
   Fox Model Loader memakai nama kelas terbaca (`com.elfmcys.yesstevemodel`), jadi lapisan ini diganti dengan rujukan langsung.
   Perlu dicek tiap simbol yang dipakai terhadap jar Fox Model Loader.
4. 29 mixin: sebagian menarget Epic Fight (nama kelas sama di port Fabric, tetapi perlu dicek tiap target), sebagian menarget YSM
   lewat alias/mapping (harus diarahkan ulang ke kelas Fox Model Loader), sebagian menarget Configured/TouhouLittleMaid (dibuang dulu).
5. Integrasi opsional (Configured, ParCool, SWEM, TLM, Oculus): dibuang dulu, ditambah belakangan.
6. Menghidupkan kembali `mixins.json` (sengaja belum ada supaya jar tahap 1 tidak bisa crash saat dimuat).


## Tahap 2 (zip ini)
- Shim sendiri `fabric/shim`: Dist, OnlyIn, ModList, FMLPaths, ServerLifecycleHooks, event bus (`bus/`) dan kelas event bergaya NeoForge (`event/`).
  (Catatan: pengalihan tahap 1 ke shim bus Epic Fight salah karena API-nya berbeda; sudah dikembalikan ke shim sendiri.)
- `FabricEventBridge`: callback Fabric -> event: login/logout, clone, respawn, ganti dimensi, start/stop tracking, server tick, server stopped,
  entity join/leave (server dan klien), tags updated, client tick, disconnect klien, perintah klien, reload listener klien.
- Entrypoint `CompatModFabric` (umum) dan `CompatModFabricClient`; config lewat Forge Config API Port; paket jaringan lewat `PayloadRegistrar` Epic Fight.
- Stub Mapping API (`net.okitsu.ysmmapping.api`) masih belum terisi. Fitur yang masih bergantung padanya mencakup roaming variables,
  ground speed query, dan sinkronisasi animasi; jalur pemilihan model, tekstur, dan audio sudah dialihkan ke API Fox Model Loader.
- Pilihan model pemain dibaca lewat `ModelInfoCapability` milik Fox Model Loader (`getModelId`, `getSelectTexture`, `isDisabled`).

## Catatan sebelum perubahan pada snapshot ini
- `mixins.json` belum diaktifkan. Mixin yang tidak bergantung pada YSM/Configured masih ada di `mixin/`; yang bergantung disimpan di `_pending/`.
  Mixin yang menarget kelas YSM resmi harus diarahkan ke kelas Fox Model Loader.
- Belum ada padanan Fabric untuk: EntityMountEvent, LivingShieldBlockEvent (perlu mixin), ScreenEvent klik, RenderPlayerEvent.Pre (CombatRenderInterceptor tidak aktif).
- Memetakan simbol YSM satu per satu di `YsmMappingApi` (lihat daftar di `YsmSymbols`).
- Belum pernah dikompilasi: dari hasil build pertama, jumlah dan jenis error baru bisa diketahui.


## Putaran 3 (dari build-log pertama dengan semua error)
Log GitHub Actions menunjukkan 174 error dari ~10 penyebab, semuanya diperbaiki:
- `IPayloadContext` Epic Fight hanya punya `player()`; compat butuh `flow()`, `enqueueWork`, `connection()` -> shim `fabric/shim/network` sendiri (+ `PayloadRegistrar`, `IPayloadHandler`).
- `ServerGamePacketListenerImpl#getConnection` (patch NeoForge) -> `isAcceptingMessages()`.
- `ClientPacketListener#hasChannel` -> `ClientPlayNetworking.canSend`.
- `ModList#getModContainerById/getModFileById` -> FabricLoader (`getModContainer`, `findPath`).
- `ElytraLayer#shouldRender` (patch NeoForge) -> `is(Items.ELYTRA)`.
- Pada tahap sebelumnya, suara model YSM belum aktif karena override streaming NeoForge tidak berlaku di Fabric.


## Perubahan statis pada snapshot ini
- Gradle Loom dipatok ke versi stabil 1.11.8. GitHub Actions sudah membangun pada push dan dapat dijalankan manual; artefak JAR hanya diunggah jika build berhasil.
- Refmap Mixin dikonfigurasi dan daftar mixin inti diaktifkan. Descriptor RenderEngine untuk first-person diperbarui dari event NeoForge ke shim Fabric Epic Fight.
- Ditambahkan bridge Mixin untuk mount entity, pre-render pemain, dan klik layar.
- Pemilihan model, tekstur, dan audio sekarang memakai API publik Fox Model Loader (`ModelInfoCapability`, `ClientModelManager`, `AudioStreamCache`, dan Fabric `FabricSoundInstance`).

## Batasan yang masih terbuka
- Build lokal sengaja tidak dijalankan sesuai permintaan pengguna; tidak ada verifikasi kompilasi pada snapshot ini. Keberhasilan GitHub Actions belum diketahui sampai project di-push dan workflow dijalankan.
- `YsmMappingApi` masih memiliki tabel simbol kosong. Roaming variables, query `ground_speed2`, dan sinkronisasi animasi belum dipetakan ke Fox Model Loader.
- Jalur pemutaran suara model sudah dialihkan ke `AudioStreamCache` dan `FabricSoundInstance`, tetapi belum dibuktikan melalui build atau uji game.
- `LivingShieldBlockEvent` belum dijembatani ke Fabric, sehingga observer sinkronisasi shield belum aktif.
- Integrasi alias YSM tertentu dan integrasi opsional TLM belum diaktifkan. Hasil runtime, termasuk kompatibilitas seluruh target Mixin, belum diuji di game.
