#
# SPDX-License-Identifier: MIT
# Copyright (c) 2020 Tano Systems LLC. All rights reserved.
#
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RRECOMMENDS:${PN}:remove = "${PN}-qmlplugins"

SRC_URI += " \
	file://GraphicsSurfaceGL_NoX.cpp.patch \
	file://0001-HACK-ANGLE-khrplatform.h-add-define-MESA_EGL_NO_X11_.patch \
"

PR:append = ".ti0"
DEPENDS += "flex-native bison-native"
