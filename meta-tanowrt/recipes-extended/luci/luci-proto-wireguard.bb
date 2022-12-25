#
# SPDX-License-Identifier: MIT
#
# Support for WireGuard VPN
#
# This file Copyright (c) 2019-2022 Tano Systems. All Rights Reserved.
# Anton Kikin <a.kikin@tano-systems.com>
#
PR = "tano3"

SUMMARY = "Support for WireGuard VPN"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit kmod/wireguard

RDEPENDS:${PN} += "wireguard-tools"
RRECOMMENDS:${PN} += "qrencode"

inherit allarch
inherit tanowrt-luci-proto

SRC_URI = "${LUCI_GIT_URI};branch=${LUCI_GIT_BRANCH};protocol=${LUCI_GIT_PROTOCOL};subpath=protocols/luci-proto-wireguard;destsuffix=git/"
SRCREV = "${LUCI_GIT_SRCREV}"
S = "${WORKDIR}/git"
