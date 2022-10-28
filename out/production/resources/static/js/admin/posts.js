'use strict';

class PostsApi {
    getPostsList(page,size) {
        let responseResult = null;

        $.ajax({
            async: false,
            type: "get",
            url: "/api/v1/posts/list/0?page="+page+"&size="+size,
            dataType: "json",
            success: (response) => {
                responseResult = response.data;
            },
            error: (error) => {
                console.log(error);
            }
        });
        return responseResult;
    }

    deletePosts(postsId) {
    let responseResult = null;

            $.ajax({
                async: false,
                type: "delete",
                url: "/api/v1/admin/"+postsId,
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
class PostsService {
    static #instance = null;

    constructor() {
    }

    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new PostsService();
        }
        return this.#instance;
    }

    getPostsList() {
        let page = null;
        let size = null;

        const urlSearch = new URLSearchParams(location.search);
        if(urlSearch == null) {
            page = 0;
            size = 10;
        } else {
            page = urlSearch.get('page');
            size = urlSearch.get('size');
        }

        const postsApi = new PostsApi();
        const postsList = postsApi.getPostsList(page,size);

        const posts = document.querySelector(".posts-list-body");
        postsList.forEach(postsList => {

        const d = new Date(postsList.date);
        const date = new Date(+d + 3240 * 10000).toISOString().split("T")[0]
        const time = d.toTimeString().split(" ")[0];

            posts.innerHTML += `
                    <tr>
                                <td>${postsList.id}</td>
                                <td>${postsList.title}</td>
                                <td>${postsList.content}</td>
                                <td>${postsList.writer}</td>
                                <td>${date+" "+time}</td>
                                <td><button value="${postsList.id}" type="button" class="delete-button">삭제</button></td>
                              </tr>
            `
        });

    }

    setDeleteButton () {
        const deleteButtons = document.querySelectorAll(".delete-button");
            deleteButtons.forEach( deleteButton => {
            deleteButton.onclick = () => {
                    if(confirm("정말로 삭제하시겠습니까?")) {
                        const postsApi = new PostsApi();
                        postsApi.deletePosts(deleteButton.value);
                    }
                }
            })
    }
}


window.onload = () => {
    PostsService.getInstance().getPostsList();
    PostsService.getInstance().setDeleteButton();
}