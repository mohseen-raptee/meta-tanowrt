#
# SPDX-License-Identifier: MIT
# Copyright (c) 2020 Tano Systems LLC. All rights reserved.
#
require recipes-image/images/tanowrt-image-full.bb

PR:append = ".0"

TANOWRT_IMAGE_QT5_INSTALL_EXAMPLES ?= "1"

TANOWRT_IMAGE_QT5_PACKAGES_CORE ?= "\
	qtbase \
	qtsvg \
	${@oe.utils.conditional('TANOWRT_IMAGE_QT5_INSTALL_EXAMPLES', '1', 'qtbase-examples', '', d)} \
"

TANOWRT_IMAGE_QT5_PACKAGES_FONTS ?= "\
	ttf-dejavu-sans \
	ttf-dejavu-sans-mono \
	ttf-dejavu-sans-condensed \
	ttf-dejavu-serif \
	ttf-dejavu-serif-condensed \
	ttf-dejavu-common \
	liberation-fonts \
"

TANOWRT_IMAGE_QT5_PACKAGES_WEBKIT ?= "\
	qtwebkit \
	${@oe.utils.conditional('TANOWRT_IMAGE_QT5_INSTALL_EXAMPLES', '1', 'qtwebkit-tools', '', d)} \
"

TANOWRT_IMAGE_QT5_PACKAGES_EXTRA ?= ""

CORE_IMAGE_EXTRA_INSTALL += "\
	${TANOWRT_IMAGE_QT5_PACKAGES_CORE} \
	${TANOWRT_IMAGE_QT5_PACKAGES_FONTS} \
	${TANOWRT_IMAGE_QT5_PACKAGES_WEBKIT} \
	${TANOWRT_IMAGE_QT5_PACKAGES_EXTRA} \
"
