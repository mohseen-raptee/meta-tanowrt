#
# SPDX-License-Identifier: MIT
#
# Tano Systems LuCI Theme bitbake recipe
# Copyright (c) 2019-2022, Tano Systems LLC. All rights reserved.
#
PR = "tano45"
PV = "0.0.9+git${SRCPV}"

SUMMARY = "LuCI Theme by Tano Systems"
HOMEPAGE = "https://github.com/tano-systems/luci-theme-tano"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=572ff0d89ff068a9a5f7b4bc844b09cc"

DEPENDS += "nodejs-native"

inherit tanowrt-luci-theme
inherit tanowrt-luci-i18n
inherit python3native

LUCI_THEME_NAME = "tano"

LUCI_THEME_TANO_GIT_URI      ?= "git://github.com/tano-systems/luci-theme-tano.git"
LUCI_THEME_TANO_GIT_BRANCH   ?= "master"
LUCI_THEME_TANO_GIT_PROTOCOL ?= "https"
LUCI_THEME_TANO_GIT_SRCREV   ?= "f7c647300351fe255039db2262562f1de395f25d"

SRC_URI = "${LUCI_THEME_TANO_GIT_URI};name=theme;branch=${LUCI_THEME_TANO_GIT_BRANCH};protocol=${LUCI_THEME_TANO_GIT_PROTOCOL}"
SRCREV_theme = "${LUCI_THEME_TANO_GIT_SRCREV}"
SRCREV_FORMAT = "theme"

SRC_URI += "file://package-lock.json"

# Patches
SRC_URI += "file://0001-Upgrade-packages-versions.patch"

S = "${WORKDIR}/git"
LUCI_PKG_SRC = "${S}/bundle/build"

export HOME = "${WORKDIR}"

do_npm_configure() {
	cd ${B}
	cp -vf ${WORKDIR}/package-lock.json ${B}/package-lock.json
	npm cache clean --force
	npm install
}

do_npm_compile() {
	cd ${B}
	npm --verbose run bundle
}

addtask npm_configure after do_patch before do_npm_compile
addtask npm_compile after do_npm_configure before do_install

# Make sure we have native npm ready when we start building
do_npm_configure[depends] += "nodejs-native:do_populate_sysroot"
