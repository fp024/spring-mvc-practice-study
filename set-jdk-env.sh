#!/bin/sh
# set-jdk-env.bat의 Linux/macOS 대응 스크립트.
#
# JAVA_HOME은 자식 프로세스에서 부모 셸로 되돌릴 수 없으므로 반드시 source로 실행한다.
#   . ./set-jdk-env.sh
#
# setenv-custom.properties가 있으면 그 파일을, 없으면 setenv.properties를 사용한다.
# (set-jdk-env.bat과 동일한 규칙)

case "$(basename -- "$0")" in
    set-jdk-env.sh)
        echo "[WARN] 직접 실행하면 JAVA_HOME이 현재 셸에 남지 않습니다." >&2
        echo "[WARN] '. ./set-jdk-env.sh' 처럼 source로 실행하세요." >&2
        ;;
esac

# source로 실행되면 $0는 호출한 셸/스크립트를 가리키므로, 셸별 변수를 먼저 살펴본다.
set_jdk_env_main() {
    _sje_dir=$(dirname -- "${BASH_SOURCE:-${ZSH_ARGZERO:-$0}}")
    [ -n "$_sje_dir" ] || _sje_dir="."
    [ -f "${_sje_dir}/setenv.properties" ] || _sje_dir="$PWD"

    if [ -f "${_sje_dir}/setenv-custom.properties" ]; then
        _sje_env_file="${_sje_dir}/setenv-custom.properties"
    else
        _sje_env_file="${_sje_dir}/setenv.properties"
    fi

    if [ ! -f "$_sje_env_file" ]; then
        echo "[ERROR] setenv-custom.properties or setenv.properties not found." >&2
        return 1
    fi

    _sje_generator="${_sje_dir}/build-scripts/generate-toolchains.sh"
    if [ ! -f "$_sje_generator" ]; then
        echo "[ERROR] Generator script not found: $_sje_generator" >&2
        return 1
    fi

    # 실행 권한이 없는 체크아웃에서도 동작하도록 sh로 직접 호출한다.
    _sje_java_home=$(sh "$_sje_generator" -p "$_sje_env_file" --print-jdk-home) || return 1

    JAVA_HOME="$_sje_java_home"
    export JAVA_HOME
    echo "JAVA_HOME=$JAVA_HOME"

    if ! sh "$_sje_generator" -p "$_sje_env_file" -o "${_sje_dir}/toolchains.xml"; then
        echo "[ERROR] Failed to generate toolchains.xml." >&2
        return 1
    fi
}

set_jdk_env_main
_sje_status=$?

unset -f set_jdk_env_main
unset _sje_dir _sje_env_file _sje_generator _sje_java_home

# source한 쪽이 성공 여부를 판단할 수 있도록, 이 테스트를 마지막 명령으로 둔다.
[ "$_sje_status" -eq 0 ]
