#
# SPDX-License-Identifier: MIT
#
# Support for Virtual eXtensible Local Area Network (VXLAN, RFC7348)
#
# This file Copyright (c) 2020 Tano Systems LLC. All rights reserved.
# Anton Kikin <a.kikin@tano-systems.com>
#
PR = "tano1"

SUMMARY = "Support for Virtual eXtensible Local Area Network (VXLAN, RFC7348)"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS:${PN} += "proto-vxlan"

inherit allarch
inherit tanowrt-luci-proto

SRC_URI = "${LUCI_GIT_URI};branch=${LUCI_GIT_BRANCH};protocol=${LUCI_GIT_PROTOCOL};subpath=protocols/luci-proto-vxlan;destsuffix=git/"
SRCREV = "${LUCI_GIT_SRCREV}"
S = "${WORKDIR}/git"
