#
# SPDX-License-Identifier: MIT
#
# LuCI support for MSTP daemon
#
# Copyright (c) 2018-2021 Tano Systems LLC. All rights reserved.
# Anton Kikin <a.kikin@tano-systems.com>
#
PV = "2.1.1+git${SRCPV}"
PR = "tano0"

SUMMARY = "LuCI support for MSTP daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aed2cf5a7c273a7c2dcdbd491a3a8416"

LUCI_DO_MINIFY_CSS = "1"

GIT_BRANCH   = "master"
GIT_SRCREV   = "d64460e24f8d7e3cfd467b2bea82291be2e4f305"
GIT_PROTOCOL = "https"
SRC_URI = "git://github.com/tano-systems/luci-app-tn-mstpd.git;branch=${GIT_BRANCH};protocol=${GIT_PROTOCOL}"

SRCREV = "${GIT_SRCREV}"

RDEPENDS:${PN} += "mstpd"

S = "${WORKDIR}/git"

inherit allarch
inherit tanowrt-luci-app
inherit tanowrt-luci-i18n

LUCI_APP_TN_MSTPD_HIDE_FOOTER ?= "1"

do_install:append() {
	install -d ${D}${sysconfdir}/uci-defaults

	UCIDEFFILE=${D}${sysconfdir}/uci-defaults/80_luci_app_tn_mstpd_footer

	echo "#!/bin/sh" > ${UCIDEFFILE}
	echo "uci -q batch <<-EOF >/dev/null" >> ${UCIDEFFILE}
	echo "    set luci.app_tn_mstpd=internal" >> ${UCIDEFFILE}
	echo "    set luci.app_tn_mstpd.hide_footer=${LUCI_APP_TN_MSTPD_HIDE_FOOTER}" >> ${UCIDEFFILE}
	echo "    commit luci" >> ${UCIDEFFILE}
	echo "EOF" >> ${UCIDEFFILE}
	echo "exit 0" >> ${UCIDEFFILE}
}
