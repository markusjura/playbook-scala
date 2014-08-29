/** @jsx React.DOM */


var Post = React.createClass({
  calcTimeLeft: function() {
    return this.props.createdAt;
  },

  render: function() {
    var profileLink = '/' + this.props.author;

    return (
      <div className="list-group-item">
        <p className="list-group-item-text" className="profile-link"><a href={profileLink}>{this.props.author}</a> {this.calcTimeLeft()}</p>
        <p className="list-group-item-text" dangerouslySetInnerHTML={{__html: this.props.message}} />
      </div>
      );
  }
});

var PostList = React.createClass({
  render: function() {
    var postNodes = this.props.posts.map(function(postWithUser) {
      return (<Post
      message={postWithUser.post.message}
      author={postWithUser.user.uid}
      createdAt={postWithUser.post.createdAt} />);
    });

    return (
      <div>
        <div className="list-group">
          <div className="list-group-item">
            <h4 className="list-group-item-heading">{this.props.title}</h4>
          </div>
          {postNodes}
        </div>
      </div>
      );
  }
});