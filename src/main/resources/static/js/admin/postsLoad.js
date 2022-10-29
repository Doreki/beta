'use strict';

window.onload = () => {
    PostsViewService.getInstance().loadPostsList();
    PostsDeleteService.getInstance().setDeleteButton();
}