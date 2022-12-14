#
# SPDX-License-Identifier: MIT
#
# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Copyright (C) 2018 Daniel Dickinson <cshored@thecshore.com>
# Copyright (C) 2018-2022 Anton Kikin <a.kikin@tano-systems.com>
#

PR = "tano5"
DESCRIPTION = "C utility functions for OpenWrt"
HOMEPAGE = "http://git.openwrt.org/?p=project/libubox.git;a=summary"

PACKAGECONFIG ??= "lua examples"

PACKAGECONFIG[lua] = ""
PACKAGECONFIG[examples] = ""

PROVIDES += "libblobmsg-json jshn libjson-script"
RPROVIDES:${PN} += "libblobmsg-json jshn libjson-script"

LICENSE = "BSD-1-Clause&BSD-3-Clause"
LICENSE += "${@bb.utils.contains('PACKAGECONFIG', 'examples', '&GPL-2.0-only', '', d)}"
LICENSE:${PN} = "BSD-1-Clause&BSD-3-Clause"
LICENSE:${PN}-lua = "BSD-1-Clause&BSD-3-Clause"
LICENSE:${PN}-examples = "GPL-2.0-only&BSD-1-Clause&BSD-3-Clause"

LIC_FILES_CHKSUM = "\
                   file://avl.c;endline=39;md5=00810155fed3d604816ec5814523d60a \
                   file://avl-cmp.c;endline=15;md5=1603e6094b432a5f3f320877a06f41b5 \
                   file://base64.c;endline=61;md5=51fdff010d45b0086ac0a6e035693dc0 \
                   file://blobmsg.c;endline=15;md5=7ed64c1570e8c9b46c4fc6fbd16c489e \
                   file://list.h;endline=28;md5=2d5f5475fbd0f08741354c5a99c2e983 \
                   file://md5.h;endline=39;md5=048bf9f68963c207a0c2b3a94c9d2aaa \
                   file://md5.c;endline=51;md5=0a448eea0bcbc89e3c7e6608f2d119a0 \
                   file://usock.h;endline=18;md5=f0dfdc8de858e66d66d74036611bba14 \
                   file://uloop.c;beginline=1;endline=17;md5=f151c0422668fa4c8f91d2caf5267b3e \
                   "

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

SECTION = "libs"

DEPENDS += "json-c"
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'lua', 'lua5.1', '', d)}"

SRC_URI = "\
          git://${GIT_OPENWRT_ORG}/project/libubox.git;branch=master \
          file://0001-version-libraries.patch \
          file://0002-ustream-Do-not-freeze-uloop-for-large-size-reads.patch \
          file://fix-libdir.patch \
          "

# 10.02.2022
# blob: clear buf->head when freeing a buffer
SRCREV = "f2d6752901f2f2d8612fb43e10061570c9198af1"
PV = "20211120"

S = "${WORKDIR}/git"

inherit cmake pkgconfig tanowrt-lua

EXTRA_OECMAKE += "\
                -DBUILD_LUA=${@bb.utils.contains('PACKAGECONFIG', 'lua', 'ON', 'OFF', d)} \
                -DBUILD_EXAMPLES=${@bb.utils.contains('PACKAGECONFIG', 'examples', 'ON', 'OFF', d)} \
                -DCMAKE_SKIP_RPATH=ON \
                "

OECMAKE_C_FLAGS += "${@bb.utils.contains('PACKAGECONFIG', 'lua', '-I${STAGING_INCDIR}/lua5.1', '', d)}"

do_install:append() {
    install -d ${D}${bindir} ${D}${includedir}/libubox
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'examples', 'ON', 'OFF', d)}" = "ON" ]; then
        install -m 0755 ${B}/examples/*-example ${D}${bindir}
        install -m 0755 ${S}/examples/uloop_pid_test.sh ${D}${bindir}
        if [ "${@bb.utils.contains('PACKAGECONFIG', 'lua', 'ON', 'OFF', d)}" = "ON" ]; then
            install -m 0755 ${S}/examples/uloop-example.lua ${D}${bindir}
        fi
        install -m 0755 ${S}/examples/uloop_pid_test.sh ${D}${bindir}
    fi
    install -m 0644 ${S}/*.h ${D}${includedir}/libubox
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'lua', 'ON', 'OFF', d)}" = "ON" ]; then
        install -m 0755 ${B}/lua/uloop.so ${D}${libdir}/lua/5.1/uloop.so
    fi
}

PACKAGES =+ "\
            ${@bb.utils.contains('PACKAGECONFIG', 'examples', '${PN}-examples', '', d)} \
            ${@bb.utils.contains('PACKAGECONFIG', 'lua', '${PN}-lua', '', d)} \
            "

FILES:${PN} += "${datadir}/*"
FILES:${PN}-lua += "${libdir}/lua/5.1/*"
FILES:${PN}-examples += "${bindir}/*-example \
                        ${bindir}/uloop-example.lua \
                        ${bindir}/uloop_pid_test.sh \
                        "

BBCLASSEXTEND = "native"
