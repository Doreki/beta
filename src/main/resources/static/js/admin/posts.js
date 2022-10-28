'use strict';

class PostsApi {
    static #instance = null;

    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new PostsApi();
        }
        return this.#instance;
    }

    getPostsList(page) {
        let responseResult = null;

        $.ajax({
            async: false,
            type: "get",
            url: "/api/v1/admin/posts/list",
            data: {
                "page": page,
                "size":10
            },
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

class PageHandler {
    #page = 0;
    #maxPageNumber = 0;
    #pageNumberList = null;

    constructor(page,totalCount) {
        this.#page = page;
        this.#maxPageNumber = totalCount / 10 == 0 ? (totalCount < 10 ? 1: Math.floor(totalCount / 10)) : Math.floor(totalCount / 10)+1;
        this.#pageNumberList = document.querySelector(".page-number-list");
        this.#pageNumberList.innerHTML = "";
        this.loadPageNumbers();
    }

    loadPageNumbers() {
            this.createPreButton();
            this.createNumberButtons();
            this.createNextButton();
            this.addPageButtonEvent();
            this.setColorButton();
    }

    createPreButton() {
        const nowPage = this.#page+1;
        const startIndex = nowPage % 10 ==0 ? (nowPage ==0 ? 1 : nowPage - 9) : nowPage - (nowPage % 10) + 1;
            if(startIndex !=1) {
                this.#pageNumberList.innerHTML +=`
                <a href="javascript:void(0)"><li>&#60;</li></a>
                `;
       }
    }

    createNumberButtons() {
        const nowPage = this.#page+1;
        const startIndex = nowPage % 10 ==0 ? (nowPage ==0 ? 1 : nowPage - 9) : nowPage - (nowPage % 10) + 1;
        const endIndex = startIndex + 9 <= this.#maxPageNumber ? startIndex + 9 : this.#maxPageNumber;

            for(let i = startIndex; i <= endIndex; i++) {
                this.#pageNumberList.innerHTML += `
                <a href="javascript:void(0)"><li>${i}</li></a>
                `;
        }
    }
    createNextButton() {
        const nowPage = this.#page+1;
        const startIndex = (nowPage % 10 ==0 ? (nowPage ==0 ? 1 : nowPage - 9) : nowPage - (nowPage % 10) + 1);
        const endIndex = startIndex + 9 <= this.#maxPageNumber ? startIndex + 9 : this.#maxPageNumber;
            if(endIndex != (this.#maxPageNumber/10*10)) {
                this.#pageNumberList.innerHTML +=`
                <a href="javascript:void(0)"><li>&#62;</li></a>
                `;
       }
    }

    addPageButtonEvent() {
            const pageButtons = this.#pageNumberList.querySelectorAll("li");
            pageButtons.forEach(button => {
                button.onclick = () => {
                     const nowPage = Number(PostsService.getInstance().pageHandler.page);
                     console.log(nowPage);
                    if(button.textContent == "<") {
                        PostsService.getInstance().pageHandler.page = ((nowPage+1)%10 == 0 ? Number(nowPage+1)-10 : (Math.floor(nowPage/10)*10))-1;
                        PostsService.getInstance().loadPostsList();
                    } else if (button.textContent == ">") {
                        PostsService.getInstance().pageHandler.page = ((nowPage+1)%10 == 0 ? Number(nowPage+1) : (Math.floor(nowPage/10)*10+10));
                        PostsService.getInstance().loadPostsList();
                    } else {
                        if(button.textContent != nowPage+1) {
                            PostsService.getInstance().pageHandler.page = button.textContent-1;
                            PostsService.getInstance().loadPostsList();
                        }
                    }
                }
            });
        }

    setColorButton() {
        const pageButtons = this.#pageNumberList.querySelectorAll("li");
        const nowPage = PostsService.getInstance().pageHandler.page;
        pageButtons.forEach(button => {
            if(button.textContent == nowPage+1) {
            button.classList.add("page-button");
            }
        })

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

    pageHandler = {
        page: 0,
        totalCount: 0
    }

    loadPostsList() {
        const responseData = PostsApi.getInstance().getPostsList(this.pageHandler.page);
        this.pageHandler.totalCount = responseData.totalCount;

        new PageHandler(this.pageHandler.page, this.pageHandler.totalCount);
        this.getPostsList(responseData.postsResponseDtos);
    }

    getPostsList(responseData) {

        const posts = document.querySelector(".posts-list-body");
        posts.innerHTML = ``;

        responseData.forEach(postsList => {

        const encodedDate = this.encodeDateTime(postsList.date);

        posts.innerHTML += `
                    <tr>
                                <td>${postsList.postsId}</td>
                                <td>${postsList.title}</td>
                                <td>${postsList.content}</td>
                                <td>${postsList.writer}</td>
                                <td>${encodedDate}</td>
                                <td><button value="${postsList.postsId}" type="button" class="delete-button">삭제</button></td>
                              </tr>
            `
        });

    }

    encodeDateTime(dateByResponseData) {
        const date = new Date(dateByResponseData);
        const encodedDate = new Date(date+ 3240 * 10000).toISOString().split("T")[0];
        const time = date.toTimeString().split(" ")[0];
        return encodedDate+" "+time;
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
    PostsService.getInstance().loadPostsList();
    PostsService.getInstance().setDeleteButton();
}