#
# SPDX-License-Identifier: MIT
# Copyright (c) 2020-2021 Tano Systems LLC. All rights reserved.
#
PR:append = ".tano3"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

SRC_URI += "\
	file://logread \
	file://rsyslog.init \
	file://rsyslog.conf \
	file://syslogrotate \
"

# Patches
SRC_URI += "\
	file://0001-Add-support-for-monthname-timestamp-format.patch \
"

RDEPENDS:${PN}:remove = "logrotate"

PACKAGECONFIG ??= " \
	rsyslogd \
	rsyslogrt \
	klog \
	inet \
	regexp \
	imdiag \
	imfile \
	${@bb.utils.filter('DISTRO_FEATURES', 'snmp systemd', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'ptest', 'testbench relp ${VALGRIND}', '', d)} \
"

inherit update-alternatives

ALTERNATIVE:${PN} = "logread"

ALTERNATIVE_PRIORITY = "50"
ALTERNATIVE_LINK_NAME[logread] = "${sbindir}/logread"

do_install:append() {
	# Remove sysvinit and install procd init script
	rm -f ${D}${sysconfdir}/init.d/syslog
	install -m 0755 ${WORKDIR}/rsyslog.init ${D}${sysconfdir}/init.d/rsyslog

	rm -f ${D}${sysconfdir}/logrotate.d/logrotate.rsyslog

	install -d ${D}${sysconfdir}/rsyslog.d

	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/logread ${D}${sbindir}/logread

	install -d ${D}${base_sbindir}
	ln -s ${sbindir}/logread ${D}${base_sbindir}/logread

	install -d ${D}${libexecdir}
	install -m 0755 ${WORKDIR}/syslogrotate ${D}${libexecdir}/syslogrotate
}

FILES:${PN} += "${libexecdir}"

inherit tanowrt-services

TANOWRT_SERVICE_PACKAGES = "rsyslog"
TANOWRT_SERVICE_SCRIPTS_rsyslog += "rsyslog"
TANOWRT_SERVICE_STATE_rsyslog-rsyslog ?= "enabled"
