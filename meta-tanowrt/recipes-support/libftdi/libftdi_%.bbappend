#
# SPDX-License-Identifier: MIT
# Copyright (c) 2019-2020 Tano Systems LLC. All rights reserved.
#
PR:append = ".tano0"

EXTRA_OECMAKE += "-DEXAMPLES=on -DCMAKE_SKIP_RPATH=TRUE"

# EEPROM tool
PACKAGES =+ "ftdi-eeprom"
PACKAGECONFIG += "ftdi-eeprom"
PACKAGECONFIG[ftdi-eeprom] = "-DFTDI_EEPROM=on,-DFTDI_EEPROM=off,libconfuse"
FILES:ftdi-eeprom += "${bindir}/ftdi_eeprom"
RDEPENDS:ftdi-eeprom += "libftdi"

# Examples
PACKAGES =+ "ftdi-examples"
RDEPENDS:ftdi-examples += "libftdi"

do_install:append() {
	install -dm 0755 ${D}${bindir}
	install -m 0755 ${B}/examples/baud_test ${D}${bindir}/
	install -m 0755 ${B}/examples/bitbang ${D}${bindir}/
	install -m 0755 ${B}/examples/bitbang_cbus ${D}${bindir}/
	install -m 0755 ${B}/examples/bitbang_ft2232 ${D}${bindir}/
	install -m 0755 ${B}/examples/bitbang2 ${D}${bindir}/
	install -m 0755 ${B}/examples/eeprom ${D}${bindir}/
	install -m 0755 ${B}/examples/find_all ${D}${bindir}/
	install -m 0755 ${B}/examples/serial_test ${D}${bindir}/
	install -m 0755 ${B}/examples/simple ${D}${bindir}/
	install -m 0755 ${B}/examples/stream_test ${D}${bindir}/
}

FILES:ftdi-examples += "\
	${bindir}/baud_test \
	${bindir}/bitbang \
	${bindir}/bitbang_cbus \
	${bindir}/bitbang_ft2232 \
	${bindir}/bitbang2 \
	${bindir}/eeprom \
	${bindir}/find_all \
	${bindir}/serial_test \
	${bindir}/simple \
	${bindir}/stream_test \
"
