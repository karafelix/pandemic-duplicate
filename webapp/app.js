app = angular.module('pandemicDuplicateApp', []);

app.factory('StateService',
  function() {
    return {
      go: function(rel_url) {
        if (rel_url != null) {
          history.pushState(null, null, BASE_URL + '#/' + rel_url);
        } else {
          history.pushState(null, null, BASE_URL);
        }
        on_state_init();
      }
    };
  });

app.controller('TopController',
  function($scope, StateService) {
    this.goto_state_async = function(rel_url) {
      $scope.$apply(function() {
        StateService.go(rel_url);
      });
    };
    window.addEventListener('popstate', function() {
      $scope.$apply(on_state_init);
    });
    on_state_init();
  });

app.controller('WelcomePageController',
  function(StateService) {
    this.start_game_clicked = function() {
      StateService.go('params');
    };

    this.resume_game_clicked = function() {
      var game_id = localStorage.getItem(PACKAGE + '.current_game');
      if (game_id == null) {
        return false;
      }

      var time_str = localStorage.getItem(PACKAGE + '.game.' + game_id + '.time');
      if (time_str != null) {
        StateService.go(game_id + '/T' + time_str);
      }
      else {
        StateService.go(game_id + '/player_state');
      }

      return;
    };

    this.review_results_clicked = function() {
      StateService.go('review_results');
    };

    this.join_network_game_clicked = function() {
      StateService.go('join_network_game');
    };

    this.options_clicked = function() {
      StateService.go('options');
    };

  });

app.controller('OptionsPageController',
  function() {
  });

app.controller('SubscriptionPageController',
  function() {
  });

app.controller('CreateGamePageController',
  function() {
  });

app.controller('GenerateGamePageController',
  function() {
  });

app.controller('JoinGamePageController',
  function() {
  });

app.controller('JoinGamePickPageController',
  function() {
  });

app.controller('PickScenarioPageController',
  function() {
  });

app.controller('ReviewResultsPageController',
  function() {
  });

app.controller('FoundCompletedGamesPageController',
  function() {
  });

app.controller('PlayerNamesPageController',
  function() {
  });

app.controller('DeckSetupPageController',
  function() {
  });

app.controller('BoardSetupPageController',
  function() {
  });

app.controller('PlayerSetupPageController',
  function() {
  });

app.controller('PlayerTurnPageController',
  function() {
  });

app.controller('DiscoverCurePageController',
  function() {
  });

app.controller('VirulentStrainPageController',
  function() {
  });

app.controller('SpecialEventPageController',
  function() {
  });

app.controller('NewAssignmentPageController',
  function() {
  });

app.controller('ResilientPopulationPageController',
  function() {
  });

app.controller('InfectionRumorPageController',
  function() {
  });

app.controller('ForecastPageController',
  function() {
  });

app.controller('ResourcePlanningPageController',
  function() {
  });

app.controller('GameCompletedPageController',
  function() {
  });

app.controller('ResultsPageController',
  function() {
  });

app.controller('ShowDiscardsPageController',
  function() {
  });