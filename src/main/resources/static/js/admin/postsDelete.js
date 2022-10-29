'use strict';

class PostsDeleteApi {
    static #instance = null;

    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new PostsDeleteApi();
        }
        return this.#instance;
    }

    deletePosts(postsId) {
        let responseResult = null;

                $.ajax({
                    async: false,
                    type: "delete",
                    url: "/api/v1/admin/posts/"+postsId,
                    dataType: "json",
                    success: (response) => {
                        responseResult = response.msg;
                        alert(responseResult);
                        location.reload();
                    },
                    error: (error) => {
                        responseResult = error.responseJSON.data.errMsg;
                        alert(responseResult);
                    }
                });
        }
}

class PostsDeleteService {
    static #instance = null;

    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new PostsDeleteService();
        }
        return this.#instance;
    }

    setDeleteButton () {
        const deleteButtons = document.querySelectorAll(".delete-button");
            deleteButtons.forEach( deleteButton => {
            deleteButton.onclick = () => {
                if(confirm("정말로 삭제하시겠습니까?")) {
                    PostsDeleteApi.getInstance().deletePosts(deleteButton.value);
                }
            }
       })
    }
 }
