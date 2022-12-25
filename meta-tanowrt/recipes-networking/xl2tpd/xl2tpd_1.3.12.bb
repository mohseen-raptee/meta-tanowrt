#
# SPDX-License-Identifier: MIT
# Copyright (c) 2018-2020 Tano Systems LLC. All rights reserved.
#
SUMMARY = "Xelerance version of the Layer 2 Tunneling Protocol (L2TP) daemon"
HOMEPAGE = "http://www.xelerance.com/software/xl2tpd/"
SECTION = "net"
DEPENDS = "ppp virtual/kernel"

PR = "tano1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/xelerance/xl2tpd.git;branch=master;protocol=https"
SRCREV = "66fefc9c71cc7ceb3e64e85a1098dfdf1b7d0afe"

DEPENDS += "libpcap"
RDEPENDS:${PN} += "resolveip ppp-l2tp"

SRC_URI += "\
	file://l2tp.sh \
	file://xl2tpd.init \
	file://xl2tpd.conf \
	file://xl2tp-secrets \
	file://options.xl2tpd \
"

inherit tanowrt-services

TANOWRT_SERVICE_PACKAGES = "xl2tpd"
TANOWRT_SERVICE_SCRIPTS_xl2tpd += "xl2tpd"
TANOWRT_SERVICE_STATE_xl2tpd-xl2tpd ?= "enabled"

S = "${WORKDIR}/git"

do_compile () {
    oe_runmake CFLAGS="${CFLAGS} -DLINUX" LDFLAGS="${LDFLAGS}" PREFIX="${prefix}" KERNELSRC=${STAGING_KERNEL_DIR} all
}

do_install () {
    oe_runmake PREFIX="${D}${prefix}" install

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xl2tpd.init ${D}${sysconfdir}/init.d/xl2tpd

    install -d ${D}${sysconfdir}/xl2tpd
    install -m 0644 ${WORKDIR}/xl2tpd.conf ${D}${sysconfdir}/xl2tpd/xl2tpd.conf
    install -m 0644 ${WORKDIR}/xl2tp-secrets ${D}${sysconfdir}/xl2tpd/xl2tp-secrets

    install -d ${D}${sysconfdir}/ppp
    install -m 0644 ${WORKDIR}/options.xl2tpd ${D}${sysconfdir}/ppp/options.xl2tpd

    install -d ${D}${nonarch_base_libdir}/netifd/proto
    install -m 0755 ${WORKDIR}/l2tp.sh ${D}${nonarch_base_libdir}/netifd/proto/l2tp.sh
}

FILES:${PN} += "${nonarch_base_libdir}"

CONFFILES:${PN} += "\
	${sysconfdir}/xl2tpd/xl2tpd.conf \
	${sysconfdir}/xl2tpd/xl2tp-secrets \
	${sysconfdir}/ppp/options.xl2tpd \
"
