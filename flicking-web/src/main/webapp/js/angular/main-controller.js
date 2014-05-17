var IndexController = function IndexController($rootScope, $scope, MovieService, ngProgress, $facebook) {
    $rootScope.reviewers = ["Times of India", "NDTV", "Rajeev Masand","Book My Show","Rediff","Rotten Tomatoes","IMDB","Hindustan Times"];
    $scope.itemsPerPage = 5;
    $scope.currentPage = 0;

    $scope.fetchMovieList = function () {
        $scope.movies = MovieService.getlatest({
            limit: $scope.itemsPerPage,
            offset: $scope.currentPage * $scope.itemsPerPage
        });
    };

    $scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
        }
    };

    $scope.prevPageDisabled = function () {
        return $scope.currentPage === 0 ? "disabled" : "";
    };

    $scope.nextPage = function () {
        $scope.currentPage++;
    };

    $scope.nextPageDisabled = function () {
        return "";
    };

    $scope.$watch("currentPage", function (newValue, oldValue) {
        $scope.fetchMovieList();
    });


    //fb share
    $scope.share = function (movie) {    

        $facebook.ui({
            method: 'feed',
            name: movie.movieName,
            caption: 'Overall Rating ' + movie.averageReviewerRating + ' out of 5',
            description: movie.description,
            link: 'http://www.flickingtruth.com/#/movie/' + movie.id,
            picture: movie.imageURL
        });

    };


    $scope.fetchMovieList();
    
    $scope.fetchUpcomingMovieList = function () {
        $scope.upcomingmovies = MovieService.getUpcoming({
            limit: $scope.itemsPerPage,
            offset: 0
        });
    };
    
    $scope.fetchUpcomingMovieList();


};

var IndexMovieDetailController = function ($scope, $rootScope, $routeParams, $location, MovieService, CommentService, $facebook) {
    var id = $routeParams.id;

    $scope.movie = MovieService.get({
        id: id
    });

    $scope.fetchCommentList = function () {
        $scope.comments = CommentService.query({
            movieId: id
        });
    };

    $scope.comment = {};

    $scope.addComment = function (isValid) {
        if (isValid) {
            if (!$scope.comment.id) {
                var newComment = new CommentService($scope.comment);
                newComment.$save({
                    movieId: id
                }).then(function () {
                     $scope.comments.push(newComment);
                    toastr.success("Comment posted successfully");

                }, function (response) {
                    if (response.status == 409) {
                        toastr.error("You Have Already Commented");
                    }
                });
            }
        }

    };
    
     //fb share
    $scope.share = function (movie) {     

        $facebook.ui({
            method: 'feed',
            name: movie.movieName,
            caption: 'Overall Rating ' + movie.averageReviewerRating + ' out of 5',
            description: movie.description,
            link: 'http://www.flickingtruth.com/#/movie/' + movie.id,
            picture: movie.imageURL
           
        });

    };

    $scope.fetchCommentList();

};


var LoginController = function LoginController($scope, $rootScope, $location, $cookieStore, LoginService, ngProgress) {

    $scope.rememberMe = false;
    $scope.alert = {};

    $scope.login = function () {
        LoginService.authenticate($.param({
            username: $scope.username,
            password: $scope.password
        }), function (authenticationResult) {
            var authToken = authenticationResult.token;
           // console.log("The auth token" + authToken);
            $rootScope.authToken = authToken;
            if ($scope.rememberMe) {
                $cookieStore.put('authToken', authToken);
            }
            LoginService.get(function (user) {
                $rootScope.user = user;
                $location.path("/");
            });

        }, function () {
            $scope.alert.message = "Oops!! Invalid Email or Password";
            $scope.alert.status = "danger";

        })
    };
    
    
    $scope.credentials  = new LoginService();
    
    $scope.forgotpassword = function (isValid) {
        if (isValid) {
            $scope.credentials.$forgotpassword().then(function () {
                $scope.alert.message = "Password reset successful, a mail with new password has been sent to your id";
                $scope.alert.status = "success";                
            }, function (response) {
                 $scope.alert.message = response.data.message;
                $scope.alert.status = "danger";              
            });

        }
    };
};

var RegisterController = function ($scope, $http, $location, LoginService) {
    $scope.user = new LoginService();
    $scope.alert = {};

    $scope.register = function (isValid) {
        if (isValid) {
            $scope.user.$register().then(function () {
                $scope.alert.message = "Congrats !! Your Account Has Been Created Successfully";
                $scope.alert.status = "success";
                $scope.user = {};
                $scope.confirm_password = "";
            }, function (response) {
                $scope.alert.message = response.data.message;
                $scope.alert.status = "danger";
            });

        }
    };

};

var UserController = function ($scope, $http, $location, UserService) {

    $scope.fetchUserList = function () {
        $scope.users = UserService.query();
    };

    $scope.editUser = function (user) {
        if (user === 'new') {
            $scope.newUser = true;
            $scope.user = {};
        } else {
            $scope.newUser = false;
            $scope.user = user;
            $scope.user.password = "default";
        }
    };

    $scope.saveUser = function () {
        if (!$scope.user.id) {
            var newUser = new UserService($scope.user);
            newUser.$save(function () {
                $scope.users.push(newUser);
            });
        } else {
            $scope.users.forEach(function (e) {
                if (e.id === $scope.user.id) {
                    e.$update();
                }
            });
        }
    };


    $scope.updatePassword = function (user) {
        $scope.user = user;
        $scope.user.newpassword = "";

    };

    $scope.setPassword = function () {
        UserService.setpassword({
            id: $scope.user.id
        }, {
            oldPassword: "",
            newPassword: $scope.user.newpassword
        });
    };

    $scope.fetchUserList();

};



var MovieController = function ($scope, $rootScope, $http, $location, MovieService) {
    $scope.reviewers = $rootScope.reviewers;
    $scope.movie = new MovieService();
    $scope.movie.reviews = [];

    //fetch movie list
    $scope.fetchMovieList = function () {
        $scope.movies = MovieService.query();
    };

    $scope.deleteMovie = function (movie) {
        movie.$delete().then(function () {
            toastr.success("Movie Deleted Successfully");
            $scope.movies.splice($scope.movies.indexOf(movie), 1);
        });
    };

    $scope.submitForm = function (isValid) {
        //console.log("Submit Form" + isValid);

        // check to make sure the form is completely valid
        if (isValid) {
            $scope.movie.$save().then(function (data) {
                toastr.success("Movie Added Successfully");
                $scope.movies.push(data);
                $location.path("/movies");
            }).
            catch (function (req) {
                toastr.error("Error Saving Movie");
            });
        }

    };


    $scope.addReview = function () {
        //console.log("Add Element");
        var newReview = {};
        $scope.movie.reviews.push(newReview);
    };

    $scope.removeReview = function ($index) {
        $scope.movie.reviews.splice($index, 1);
    }


    $scope.save = function () {
        $scope.movie.$save().then(function (data) {
            toastr.success("Movie Added Successfully");
            $scope.movies.push(data);
            $location.path("/movies");
        }).
        catch (function (req) {
            toastr.error("Error Savimg Movie");
        });
    };


    //open data control       
    /*
    $scope.disabled = function (date, mode) {
        return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
    };

    $scope.dateOptions = {
        'year-format': "'yy'",
        'starting-day': 1
    };*/


    $scope.openDate = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };


    $scope.fetchMovieList();


    //tab handling




};

var MovieDetailController = function ($scope, $rootScope, $routeParams, $location, MovieService, $modal, ReviewService) {
    $scope.reviewers = $rootScope.reviewers;
    var id = $routeParams.id;
    //console.log("Route" + id);

    $scope.movie = MovieService.get({
        id: id
    });

    //date

    $scope.openDate = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };


    $scope.updateMovie = function (isValid) {
        if (isValid) {
            $scope.movie.$update().then(function (data) {
                toastr.success("Movie Updated Successfully");
                //$scope.movies.push(data);
                $location.path("/movies");
            }).
            catch (function (req) {
                toastr.error("Error Updating Movie");
            });
        }
    };

    $scope.activeTab = 'movie';
    $scope.tabTemplates = {
        movie: 'views/movie-detail.html',
        reviews: 'views/review-list.html'
    };

    $scope.switchTabTo = function (tabId) {
        $scope.activeTab = tabId;
        /* other stuff to do */
    };

    $scope.reviews = [];
    //handling reviews
    ReviewService.query({
        movieId: id
    }, function (reviews) {
        $scope.reviews = reviews;
    });



    $scope.editReview = function (event) {

        //console.log("Inside edit review" + event);
        $scope.opts = ['on', 'off'];

        if (event === 'new') {
            $scope.newEvent = true;
            $scope.review = {};
        } else {
            $scope.newEvent = false;
            $scope.review = event;
        }
    };



    $scope.saveReview = function () {
        // console.log("Indise Save Review");
        if (!$scope.review.id) {
            var newReview = new ReviewService($scope.review);
            newReview.$save({
                movieId: id
            }, function () {
                $scope.reviews.push(newReview);
            });
        } else {
            $scope.reviews.forEach(function (e) {
                //console.log("Inside Update");
                if (e.id === $scope.review.id) {
                    e.$update({
                        movieId: id
                    });
                }
            });
        }
    };

    $scope.deleteReview = function () {
        $scope.reviews.forEach(function (e, index) {
            if (e.id == $scope.review.id) {
                $scope.review.$delete({
                    movieId: id,
                    id: $scope.review.id
                }, function () {
                    $scope.reviews.splice(index, 1);
                });
            }
        });
    };



    $scope.submitReviewForm = function (isValid) {
        //console.log("Inside Submit Review Form" + isValid);
    };
};


var UploadImageController = function ($scope, $http, $timeout, $upload, $routeParams, $location) {
    var id = $routeParams.id;
    var file = null;
    $scope.onFileSelect = function ($files) {
        file = $files[0];
        //console.log("File Selected"+file);

    };
    $scope.upload = function () {
        $upload.upload({
            url: baseURL + 'movies/' + id + '/image',
            file: file,
        }).progress(function (evt) {
            console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
        }).success(function (data, status, headers, config) {
            toastr.success(data);
            $location.path("/movies");
        });
    };
};

var ContactController = function($scope,$location,LoginService){
    
    $scope.contact  = new LoginService();
    
    $scope.submitForm = function (isValid) {
        if (isValid) {
            $scope.contact.$contact().then(function () {
                toastr.success("Message Sent Successfully");
                $location.path("/");
            }, function (response) {
                toastr.error("Error Sending Message");
            });

        }
    };
};

var UpcomingController = function($scope,$location,MovieService){    
    $scope.itemsPerPage = 10;
    $scope.currentPage = 0;

    $scope.fetchMovieList = function () {
        $scope.movies = MovieService.getUpcoming({
            limit: $scope.itemsPerPage,
            offset: $scope.currentPage * $scope.itemsPerPage
        });
    };
    $scope.fetchMovieList();
};

function HeaderController($scope, $location) 
{ 
    $scope.isActive = function (viewLocation) { 
        return viewLocation === $location.path();
    };
}