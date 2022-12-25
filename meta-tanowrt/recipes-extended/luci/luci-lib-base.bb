#
# SPDX-License-Identifier: MIT
#
# Basic libraries for luci
#
# This file Copyright (c) 2020 Tano Systems LLC. All rights reserved.
# Anton Kikin <a.kikin@tano-systems.com>
#
PR = "tano1"

SUMMARY = "Basic libraries for luci"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "lua5.1"

RDEPENDS:${PN} += "\
	lua5.1 \
	luci-lib-nixio \
	luci-lib-ip \
	luci-lib-jsonc \
	lucihttp \
"

inherit allarch
inherit tanowrt-luci-lib
inherit tanowrt-lua

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

SRC_URI = "${LUCI_GIT_URI};branch=${LUCI_GIT_BRANCH};protocol=${LUCI_GIT_PROTOCOL};subpath=libs/luci-lib-base;destsuffix=git/"
SRCREV = "${LUCI_GIT_SRCREV}"
S = "${WORKDIR}/git"
