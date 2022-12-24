#
# SPDX-License-Identifier: MIT
#
# Baikal-M Linux Kernel
# Based on Baikal ARM64 SDK 5.4 (20220126)
#
# This file Copyright (C) 2021-2022 Tano Systems LLC. All rights reserved.
# Authors: Anton Kikin <a.kikin@tano-systems.com>
#
SECTION = "kernel"
DESCRIPTION = "Linux kernel for Baikal-M platforms"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

LINUX_VERSION ?= "5.4.156"
LINUX_KERNEL_TYPE ?= "standard"
PV = "${LINUX_VERSION}"

KERNEL_SRC_URI ?= "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-${LINUX_VERSION}.tar.xz"
KERNEL_SRC_PROTOCOL ?= "https"
KERNEL_SRC_BRANCH ?= ""
KERNEL_SRC_SRCREV ?= ""

SRC_URI[machine.sha256sum] = "06fe73e4623fcf1b3c0d0e1983d8286a2ff5b8fffbcb2163f4c01696a1c377fe"

SRC_URI += "\
	file://0001-baikal-m-Add-support-for-Baikal-M-platforms.patch \
	file://0002-baikal-m-Add-defconfigs-for-Baikal-M-platforms.patch \
	file://0003-baikal_vdu_connector-Add-include-for-module.h.patch \
	file://0004-gpu-arm-midgard-Add-TX011-SW-99002-r26p0-01rel0-sour.patch \
	file://0005-gpu-arm-midgard-Fix-building-in-Yocto.patch \
	file://0006-spi-dw-Do-not-set-DW_SPI_SER-in-dw_writer.patch \
	file://0007-spi-dw-Backport-from-kernel-5.10.46.patch \
	file://0008-net-phy-leds-Trigger-leds-only-if-PHY-speed-is-known.patch \
"

# Append to the MACHINE_KERNEL_PR so that a new SRCREV will cause a rebuild
MACHINE_KERNEL_PR:append = "tano3"
PR = "${MACHINE_KERNEL_PR}"

KERNEL_IMAGETYPE ?= "Image"

require recipes-kernel/linux/linux-tano.inc
require recipes-kernel/linux/linux-tano-baikal.inc

# Look in the generic major.minor directory for files
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.4:"

S = "${WORKDIR}/linux-${LINUX_VERSION}"
