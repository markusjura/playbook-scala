/** @jsx React.DOM */

(function () {

  var SearchResult = React.createClass({

    render: function() {
      return(
        <div>{this.props.uid}</div>
      );
    }
  });

  var Search = React.createClass({
    getInitialState: function() {
      return {
        inputValue: '',
        searchResults: []
      };
    },

    componentDidMount: function() {
      // Set search result popover settings
      $('.search-results').popover({
        trigger: 'manual',
        placement: 'bottom',
        html: true
      });
    },

    onChange: function(e) {
      // Save new input value
      var inputValue  = e.target.value;
      this.setState({inputValue: inputValue});

      // Handle input value
      if(inputValue.length > 0) {
        $.get('/search/' + inputValue, function(data) {
          // Show or hide search result popover
          if(data.length > 0 && this.state.searchResults.length == 0)
            $('.search-results').popover('show');
          else if(data.length == 0)
            $('.search-results').popover('hide');

          // Clear search results
          this.setState({searchResults: data});
        }.bind(this));
      }
      else {
        // Clear uids and hide search result popover
        this.setState({searchResults: []});
        $('.search-results').popover('hide');
      }
    },

    renderSearchResults: function() {
      var html = '';
      html += '<div class="list-group search-result">';
      $.each(this.state.searchResults, function(i, searchResult) {
        html += '<a href="/' + searchResult.uid + '" class="list-group-item">' + searchResult.name + ' @' + searchResult.uid +'</a>';
      });
      html += '</div>';

      return html;
    },

    render: function() {
      // Use pure jquery and js to render results. React HTML cannot be rendered in an twitter bootstrap popover
      $('.popover-content').empty();
      $('.popover-content').append(this.renderSearchResults());

      return(
        <form className="navbar-form navbar-left" role="search">
          <div className="form-group">
            <input type="text" className="form-control search-results" placeholder="Search"
              onChange={this.onChange} value={this.state.inputValue}
              data-toggle="popover" data-content="No results" />
          </div>
        </form>
      );
    }
  });

  React.renderComponent(
    <Search />,
    document.getElementById('search')
  );
})();