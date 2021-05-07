#!/bin/bash

user_id=
user_id="$(id -u)"

group_id=
group_id="$(id -g)"

image_tag="stroom-ui-builder"

dest_dir="/builder/shared"

echo -e "${GREEN}User ID ${user_id}${NC}"
echo -e "${GREEN}Group ID ${group_id}${NC}"

# Create a persistent vol for the home dir, idempotent
docker volume create builder-home-dir-vol

docker build -t "${image_tag}" \
  --build-arg "USER_ID=${user_id}" \
  --build-arg "GROUP_ID=${group_id}" ./docker_build_container

  #-interactive \
  #-tty \
  #--rm \
docker run \
  --rm \
  --mount "type=bind,src=/home/dev/git_work/v7stroom,dst=${dest_dir}" \
  --volume builder-home-dir-vol:/home/node \
  --workdir "${dest_dir}/stroom-ui" \
  "${image_tag}" \
  bash -c './yarnBuild.sh'
  #bash
  #bash -c 'echo $PWD; nvm --version; node --version; npm --version; npx --version; yarn --version; ./yarnBuild.sh'


#  --mount "type=bind,src=$(pwd),dst=${dest_dir}" \
