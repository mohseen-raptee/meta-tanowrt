#
# SPDX-License-Identifier: MIT
# Copyright (c) 2020 Tano Systems LLC. All rights reserved.
#
# Based on original recipe taken from meta-bbb layer
# (https://github.com/jumpnow/meta-bbb.git)
#
SUMMARY = "Initialize Qt5 environment"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "tano0"

SRC_URI = "file://qt5-env.sh file://LICENSE"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${sysconfdir}/qt5
	install -d ${D}${sysconfdir}/profile.d
	install -m 0755 ${WORKDIR}/qt5-env.sh ${D}${sysconfdir}/profile.d/
}

FILES:${PN} = "${sysconfdir}/profile.d ${sysconfdir}/qt5"
