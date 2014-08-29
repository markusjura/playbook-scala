/** @jsx React.DOM */

(function () {

  var FollowingPostList = React.createClass({
    getInitialState: function() {
      return {
        posts: []
      }
    },

    componentDidMount: function() {
      $.get('/posts/followings', function(data) {
        if(this.isMounted()) {
          this.setState({
            posts: data
          });
        }
      }.bind(this));
    },

    render: function() {
      return(
        <PostList posts={this.state.posts} title="Feed" />
      );
    }
  });

  React.renderComponent(
    <FollowingPostList />,
    document.getElementById('post-wrapper')
  );
})();
