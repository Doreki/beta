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
    #startIndex = 0;
    #endIndex = 0;

    constructor(page,totalCount) {
        this.#page = page+1;
        this.#maxPageNumber = totalCount / 10 == 0 ? (totalCount < 10 ? 1: Math.floor(totalCount / 10)) : Math.floor(totalCount / 10)+1;
        this.#pageNumberList = document.querySelector(".page-number-list");
        this.#pageNumberList.innerHTML = "";
        this.#startIndex = this.findStartIndex();
        this.#endIndex = this.findEndIndex();
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
            if(this.#startIndex !=1) {
                this.#pageNumberList.innerHTML +=`
                <a href="javascript:void(0)"><li>&#60;</li></a>
                `;
       }
    }

    createNumberButtons() {
            for(let i = this.#startIndex; i <= this.#endIndex; i++) {
                this.#pageNumberList.innerHTML += `
                <a href="javascript:void(0)"><li>${i}</li></a>
                `;
        }
    }
    createNextButton() {
            if(this.#endIndex != (this.#maxPageNumber/10*10)) {
                this.#pageNumberList.innerHTML +=`
                <a href="javascript:void(0)"><li>&#62;</li></a>
                `;
       }
    }

    addPageButtonEvent() {
            const pageButtons = this.#pageNumberList.querySelectorAll("li");
            pageButtons.forEach(button => {
                button.onclick = () => {
                    const page = Number(this.#page);
                    if(button.textContent == "<") {
                        PostsService.getInstance().pageHandler.page = (page % 10 == 0 ? page-10 : (Math.floor(page/10)*10))-1;
                        PostsService.getInstance().loadPostsList();
                    } else if (button.textContent == ">") {
                        PostsService.getInstance().pageHandler.page = (page % 10 == 0 ? page : (Math.floor((page-1)/10)*10+10));
                        PostsService.getInstance().loadPostsList();
                    } else {
                        if(button.textContent != this.#page) {
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

    findStartIndex() {
        if(this.#page % 10 == 0) {
            if(this.#page == 0) {
                return 1;
            } else {
                return this.#page - 9;
            }
        } else {
            return this.#page - (this.#page % 10)+1;
        }
    }

    findEndIndex() {
        if(this.#startIndex + 9 <= this.#maxPageNumber) {
             return this.#startIndex + 9;
         } else {
             return this.#maxPageNumber;
         }
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
                        postsApi.getInstance().deletePosts(deleteButton.value);
                    }
                }
            })
    }
}


window.onload = () => {
    PostsService.getInstance().loadPostsList();
    PostsService.getInstance().setDeleteButton();
}