#
# SPDX-License-Identifier: MIT
#
# Copyright (C) 2016 Khem Raj <raj.khem@gmail.com>
# Copyright (C) 2018-2022 Anton Kikin <a.kikin@tano-systems.com>
#

PR = "tano50"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DESCRIPTION = "OpenWrt filesystem utilities"
HOMEPAGE = "https://git.openwrt.org/?p=project/fstools.git;a=summary"
# libubi is under GPL
# libblkid-tiny is PD
# libfstools is LGPL
LICENSE = "LGPL-2.1-only & GPL-2.0-only & PD"
LIC_FILES_CHKSUM = "file://ubi.c;beginline=1;endline=17;md5=8ccc371d64f0b3a8d91065b678dc7095"
SECTION = "base"
DEPENDS += "util-linux ubus uci"

inherit kmod/fs-autofs4

# fsck.ext and fsck.vfat support
RDEPENDS:${PN} += "\
	util-linux-fsck \
	e2fsprogs \
	e2fsprogs-mke2fs \
	e2fsprogs-e2fsck \
	dosfstools \
"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/patches:${THISDIR}/${BPN}/files:"

SRC_URI = "git://${GIT_OPENWRT_ORG}/project/fstools.git;branch=master \
	file://0001-block-Validate-libubi_open-return-value.patch \
	file://0002-ubi-Add-forgotten-newlines-to-error-messages.patch \
	file://0003-Fix-UUID-reading-from-vfat.patch \
	file://0004-block-Fix-some-memory-leaks-and-unnecessary-duplicat.patch \
	file://0005-block-Add-log-message-for-unmounted-device.patch \
	file://0006-block-Fix-device-path-creation.patch \
	file://0007-libfstools-Move-get_cmdline_val-to-common.patch \
	file://0008-libfstools-Support-for-f2fs-ext4-overlay-on-a-separa.patch \
	file://0009-fstools-Configurable-overlay-partition-name.patch \
	file://0010-libfstools-Fix-64-bit-compilation-issues.patch \
	file://0011-fstools-Support-extroot-for-non-MTD-rootfs_data.patch \
	file://0012-block-config_try_load-Log-warning-instead-of-error-o.patch \
	file://0013-libubi-Fix-self-assign-warning-clang.patch \
	file://0014-Add-support-for-rootfs-partition-volume-selection.patch \
	file://0015-rootdisk-Increase-maximum-partitions-number-to-16.patch \
	file://0016-libfstools-rootdisk-Minimize-output-when-calling-vol.patch \
	file://0017-libubi-Rename-libubi.h-to-libubi-fstools.h.patch \
	file://fstab.config \
	file://fstab.init \
	file://fstab.default \
	file://snapshot \
	file://mount.hotplug \
	file://media-change.hotplug \
	file://blockd.init \
"

# 05.02.2022
# libfstools: avoid segfault in find_mount_point
SRCREV = "922f1b3c091dbe12af4ba12a76ba8563418b688c"

S = "${WORKDIR}/git"

FILES_SOLIBSDEV = ""

inherit cmake pkgconfig

inherit tanowrt-services

TANOWRT_SERVICE_PACKAGES = "fstools"
TANOWRT_SERVICE_SCRIPTS_fstools += "fstab blockd"
TANOWRT_SERVICE_STATE_fstools-fstab ?= "enabled"
TANOWRT_SERVICE_STATE_fstools-blockd ?= "enabled"

EXTRA_OECMAKE += "\
	-DCMAKE_INSTALL_LIBDIR:PATH=${libdir} \
"

# avoids build breaks when using no-static-libs.inc
DISABLE_STATIC = ""

PACKAGECONFIG ??= "extroot"

PACKAGECONFIG[extroot] = "-DCMAKE_UBIFS_EXTROOT=ON,,libubox,"
PACKAGECONFIG[ovl-rootdisk-part] = "-DCMAKE_OVL_ROOTDISK_PART=ON,,,"
PACKAGECONFIG[ovl-f2fs] = "-DCMAKE_OVL_F2FS=ON,,,f2fs-tools"

do_configure:prepend () {
	if [ -e "${S}/CMakeLists.txt" ] ; then
		sed -i -e "s:ARCHIVE DESTINATION lib:ARCHIVE DESTINATION \${CMAKE_INSTALL_LIBDIR}:g" \
		       -e "s:LIBRARY DESTINATION lib:LIBRARY DESTINATION \${CMAKE_INSTALL_LIBDIR}:g" \
		       ${S}/CMakeLists.txt
	fi
}

do_install:append() {
	install -dm 0755 ${D}/sbin
	ln -s /usr/sbin/mount_root ${D}/sbin/mount_root
	ln -s /usr/sbin/jffs2reset ${D}/sbin/jffs2reset

	install -dm 0755 ${D}/usr/sbin
	install -dm 0755 ${D}${sysconfdir}/init.d
	install -dm 0755 ${D}${sysconfdir}/config
	install -dm 0755 ${D}${sysconfdir}/uci-defaults
	install -dm 0755 ${D}${sysconfdir}/hotplug.d/block

	ln -s /usr/sbin/block ${D}/sbin/block
	ln -s /usr/sbin/blockd ${D}/sbin/blockd
#	ln -s /usr/sbin/snapshot_tool ${D}/sbin/snapshot_tool
	ln -s /sbin/block ${D}/usr/sbin/swapon
	ln -s /sbin/block ${D}/usr/sbin/swapoff

	rm -f ${D}${sbindir}/ubi

	install -m 0755 ${WORKDIR}/fstab.init ${D}${sysconfdir}/init.d/fstab
	install -m 0644 ${WORKDIR}/fstab.config ${D}${sysconfdir}/config/fstab
	install -m 0755 ${WORKDIR}/fstab.default ${D}${sysconfdir}/uci-defaults/10-fstab
	install -m 0755 ${WORKDIR}/blockd.init ${D}${sysconfdir}/init.d/blockd
#	install -m 0755 ${WORKDIR}/snapshot ${D}/sbin/snapshot
	install -m 0644 ${WORKDIR}/mount.hotplug ${D}${sysconfdir}/hotplug.d/block/10-mount
	install -m 0644 ${WORKDIR}/media-change.hotplug ${D}${sysconfdir}/hotplug.d/block/00-media-change
}

FILES:${PN} += "${libdir}/*"

OECMAKE_C_FLAGS += "${@bb.utils.contains('TOOLCHAIN', 'clang', '-Wno-unknown-warning-option', '', d)}"
