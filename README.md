# spring cloud MSA 구현 프로젝트

## Submodule

### git submodule 추가 법

> git submodule add {moudle.url} {module.name}

- .gitmodules 파일 추가
- module 이름 폴더 추가

### Submodule pull 방법
> git submodule update
- 마지막에 super module의 commit 시점의 submodule정보를 가져오는 명령어

> git submodule update --remote
- 현재 submodule의 commit log 를 땡겨오는 명령어

### Submodule 상태 확인 법
> git submodule summary

### Submodule의 Submodule을 pull 하는 방법
> git submodule update --remote --recursive

### 모든 Submodule에 명령어 실행

```bash
git submodule foreach {command}
git submodule foreach git log --oneline
```

### 모든 Submodule까지 clone 방법
- init후 submodule 땡겨오기

```bash
git submodule init {submodule.name}
git submodule update
```