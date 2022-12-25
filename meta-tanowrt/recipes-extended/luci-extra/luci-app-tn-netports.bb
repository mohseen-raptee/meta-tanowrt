#
# SPDX-License-Identifier: MIT
#
# Network ports status LuCI application
#
# Copyright (c) 2018-2022 Tano Systems LLC. All rights reserved.
# Anton Kikin <a.kikin@tano-systems.com>
#
PV = "2.0.3+git${SRCPV}"
PR = "tano2"

inherit allarch
inherit tanowrt-luci-app
inherit tanowrt-luci-i18n

RDEPENDS:${PN} += "luabitop"

SUMMARY = "Network ports status LuCI application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aed2cf5a7c273a7c2dcdbd491a3a8416"

GIT_BRANCH   = "master"
GIT_SRCREV   = "b6b737745c560c50283e7ee9d2de937ba3306428"
GIT_PROTOCOL = "https"
SRC_URI = "git://github.com/tano-systems/luci-app-tn-netports.git;branch=${GIT_BRANCH};protocol=${GIT_PROTOCOL}"

SRCREV = "${GIT_SRCREV}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"
SRC_URI += "\
	file://luci_netports.config \
"

do_install:append() {
	install -dm 0755 ${D}${sysconfdir}/config
	install -m 0644 ${WORKDIR}/luci_netports.config ${D}${sysconfdir}/config/luci_netports
}

CONFFILES:${PN} = "${sysconfdir}/config/luci_netports"
RRECOMMENDS:${PN} += "luci-app-tn-netports-hotplug"

S = "${WORKDIR}/git"
