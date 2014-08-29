/** @jsx React.DOM */

(function () {

  var ProfilePostList = React.createClass({
    getInitialState: function() {
      return {
        user: {},
        posts: []
      }
    },

    componentDidMount: function() {
      var uid = $('#uid').text();

      // Retrieve user information
      $.get('/users/' + uid, function(user) {
        if(this.isMounted()) {
          this.setState({
            user: user
          });
        }
      }.bind(this));

      // Retrieve posts of user
      var url = '/posts/users/' + $('#uid').text();
      $.get(url, function(posts) {
        if(this.isMounted()) {
          this.setState({
            posts: posts
          });
        }
      }.bind(this));
    },

    render: function() {
      return(
        <PostList posts={this.state.posts} title="Posts" />
        );
    }
  });

  React.renderComponent(
    <ProfilePostList />,
    document.getElementById('post-wrapper')
  );
})();
