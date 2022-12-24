#
# SPDX-License-Identifier: MIT
#
# This file Copyright (C) 2020 Tano Systems LLC. All rights reserved.
# Authors: Anton Kikin <a.kikin@tano-systems.com>
#
SECTION = "kernel"
DESCRIPTION = "OpenIL linux kernel for NXP platforms"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KERNEL_SRC_URI ?= "git://github.com/openil/linux.git;nobranch=1"
KERNEL_SRC_PROTOCOL ?= "https"
KERNEL_SRC_BRANCH ?= "linux-5.4.y"

# Tag: OpenIL-v1.10-linux-202012"
KERNEL_SRC_SRCREV ?= "93206fd71a8d1e033b3d469505b768051b519baf"

LINUX_VERSION ?= "5.4.3-rt1"
LINUX_KERNEL_TYPE ?= "preempt-rt"
PV = "${LINUX_VERSION}"

# Append to the MACHINE_KERNEL_PR so that a new SRCREV will cause a rebuild
MACHINE_KERNEL_PR:append = "tano0"
PR = "${MACHINE_KERNEL_PR}"

require recipes-kernel/linux/linux-tano.inc
require recipes-kernel/linux/linux-tano-openil.inc

# Look in the generic major.minor directory for files
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.4:"
