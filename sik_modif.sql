SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `antriloketcetak_smc`  (
  `nomor` varchar(4) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `tanggal` date NOT NULL,
  `jam` time NULL DEFAULT NULL,
  `jam_panggil` time NULL DEFAULT NULL,
  `no_rawat` varchar(17) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`nomor`, `tanggal`) USING BTREE,
  INDEX `antriloketcetak_smc_no_rawat_IDX`(`no_rawat`) USING BTREE,
  INDEX `antriloketcetak_smc_jam_IDX`(`jam`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `datasuplier` MODIFY COLUMN `alamat` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `nama_suplier`;

ALTER TABLE `datasuplier` MODIFY COLUMN `kota` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `datasuplier` MODIFY COLUMN `no_telp` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `kota`;

ALTER TABLE `datasuplier` MODIFY COLUMN `nama_bank` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `no_telp`;

ALTER TABLE `dokter` MODIFY COLUMN `almt_tgl` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `agama`;

ALTER TABLE `emergency_index` MODIFY COLUMN `nama_emergency` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `kode_emergency`;

ALTER TABLE `industrifarmasi` MODIFY COLUMN `alamat` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `nama_industri`;

ALTER TABLE `industrifarmasi` MODIFY COLUMN `kota` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN `alamat` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `nama_suplier`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN `kota` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN `no_telp` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `kota`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN `nama_bank` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `no_telp`;

ALTER TABLE `jns_perawatan_inap` MODIFY COLUMN `nm_perawatan` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `kd_jenis_prw`;

ALTER TABLE `pasien` MODIFY COLUMN `nm_pasien` varchar(60) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `no_rkm_medis`;

ALTER TABLE `pasien` MODIFY COLUMN `tmp_lahir` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `jk`;

ALTER TABLE `pasien` MODIFY COLUMN `nm_ibu` varchar(60) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `tgl_lahir`;

CREATE DEFINER = `root`@`%` TRIGGER `set_password_user` AFTER INSERT ON `pasien` FOR EACH ROW insert into personal_pasien values(
  new.no_rkm_medis,
  '',
  aes_encrypt(date_format(new.tgl_lahir, '%d%m%Y'), 'windi')
);

ALTER TABLE `pegawai` MODIFY COLUMN `alamat` varchar(150) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `tgl_lahir`;

ALTER TABLE `pengeluaran_harian` MODIFY COLUMN `keterangan` varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '' AFTER `nip`;

ALTER TABLE `penilaian_awal_keperawatan_ranap` MODIFY COLUMN `rpd` varchar(300) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `rps`;

CREATE TABLE `referensi_mobilejkn_bpjs_taskid_response`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `no_rawat` varchar(17) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `jenispasien` enum('JKN','NON JKN') CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `taskid` char(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `code` varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `message` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `waktu` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_no_rawat_IDX`(`no_rawat`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_waktu_IDX`(`waktu`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `resep_obat` ADD COLUMN `nama_template` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `jam_penyerahan`;

ALTER TABLE `resiko_kerja` MODIFY COLUMN `nama_resiko` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `kode_resiko`;

ALTER TABLE `skdp_bpjs` MODIFY COLUMN `terapi` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `diagnosa`;

CREATE TABLE `tampjurnal_rvpbpjs`  (
  `kd_rek` char(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `nm_rek` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `debet` double NULL DEFAULT NULL,
  `kredit` double NULL DEFAULT NULL,
  PRIMARY KEY (`kd_rek`) USING HASH,
  INDEX `nm_rek`(`nm_rek`) USING HASH,
  INDEX `debet`(`debet`) USING HASH,
  INDEX `kredit`(`kredit`) USING HASH
) ENGINE = MEMORY CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

CREATE TABLE `tampjurnal_smc`  (
  `kd_rek` char(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `nm_rek` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `debet` double NULL DEFAULT NULL,
  `kredit` double NULL DEFAULT NULL,
  `user_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ip` varchar(25) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`kd_rek`, `user_id`, `ip`) USING HASH
) ENGINE = MEMORY CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

ALTER TABLE `tokoopname` MODIFY COLUMN `kode_brng` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL FIRST;

ALTER TABLE `transfer_pasien_antar_ruang` MODIFY COLUMN `diagnosa_utama` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `ruang_selanjutnya`;

SET FOREIGN_KEY_CHECKS=1;