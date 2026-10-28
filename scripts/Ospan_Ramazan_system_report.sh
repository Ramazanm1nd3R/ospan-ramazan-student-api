#!/bin/bash
# Ospan_Ramazan_system_report.sh - system monitoring report for the DevOps midterm.
# Prints the report and appends it to the personal log file.
# Usage: ./scripts/Ospan_Ramazan_system_report.sh [log_file]

STUDENT="Ramazan Ospan"
GROUP="IT2-2310"
LOG_FILE="${1:-logs/Ospan_Ramazan_system_report.log}"

report() {
    echo "=================== System report: $STUDENT ($GROUP) ==================="
    echo "Student full name : $STUDENT"
    echo "Date and time     : $(date '+%Y-%m-%d %H:%M:%S %Z')"
    echo "Username          : $(whoami)"
    echo "Operating system  : $(uname -s) $(uname -m)"
    echo "Kernel version    : $(uname -r)"
    if [ "$(uname -s)" = "Darwin" ]; then
        echo "OS release        : $(sw_vers -productName) $(sw_vers -productVersion)"
        echo "CPU               : $(sysctl -n machdep.cpu.brand_string), $(sysctl -n hw.ncpu) cores"
        echo "Memory total      : $(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 )) GB"
        echo "Memory usage      : $(top -l 1 | awk '/PhysMem/ {sub(/^PhysMem: /, ""); print}')"
    else
        echo "CPU               : $(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2 | xargs), $(nproc) cores"
        echo "Memory usage      :"; free -h
    fi
    echo "Disk usage        :"
    df -h / | sed 's/^/    /'
    echo "Available space   : $(df -h / | awk 'NR == 2 {print $4}') free on /"
    echo "=========================================================================="
}

report | tee -a "$LOG_FILE"
echo "Report appended to $LOG_FILE"
