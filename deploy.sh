echo "> current profile for server"
CURRENT_PROFILE=$(curl -s https://uliga.site/env_profile)
echo "> $CURRENT_PROFILE"

# application.yml 파일 생성
touch ./src/main/resources/application.yml

# profile-application.yml 파일 생성
touch ./src/main/resources/profile-application.yml

# GitHub-Actions 에서 설정한 값을 application.yml 파일에 쓰기
echo "${{ secrets.APPLICATION }}" >> ./src/main/resources/application.yml

echo "${{ secrets.PROFILE_APPLICATION }}" >> ./src/main/resources/profile-application.yml